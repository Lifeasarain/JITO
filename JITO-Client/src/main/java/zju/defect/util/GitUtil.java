package zju.defect.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;



public class GitUtil {

    static List<DiffEntry> getChangedFileList(RevCommit revCommit, Repository repo){
        List<DiffEntry> returnDiffs = null;

        try {
            RevCommit previsouCommit = getPrevHash(revCommit, repo);
            if (previsouCommit == null)
                return null;
            ObjectId head = revCommit.getTree().getId();

            ObjectId oldHead = previsouCommit.getTree().getId();

            System.out.println("Printing diff between the Revisions: " + revCommit.getName() + " and " + previsouCommit.getName());


            // prepare the two iterators to compute the diff between
            try {
                ObjectReader reader = repo.newObjectReader();
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                oldTreeIter.reset(reader, oldHead);
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                newTreeIter.reset(reader, head);

                // finally get the list of changed files
                Git git = new Git(repo);
                List<DiffEntry> diffs = git.diff()
                        .setNewTree(newTreeIter)
                        .setOldTree(oldTreeIter)
                        .call();
                for (DiffEntry entry : diffs) {
                    System.out.println("Entry: " + entry);
                }
                returnDiffs = diffs;
            }catch (GitAPIException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDiffs;
    }

    public static RevCommit getPrevHash(RevCommit commit, Repository repo)  throws  IOException {

        RevWalk walk = new RevWalk(repo);
        // Starting point
        walk.markStart(commit);
        int count = 0;
        for (RevCommit rev : walk) {
            // got the previous commit.
            if (count == 1) {
                return rev;
            }
            count++;
        }
        walk.dispose();

        //Reached end and no previous commits.
        return null;
    }

    public static List<String> getBugFilePath(String repoPath, String commitHash){
        int count = 0;
        List<String> bugFiles = new ArrayList<String>();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.setMustExist(true);
        builder.addCeilingDirectory(new File(repoPath));
        builder.findGitDir(new File(repoPath));
        Repository repo;
        try {
            repo = builder.build();
            RevWalk walk = new RevWalk(repo);
            ObjectId versionId=repo.resolve(commitHash);
            RevCommit verCommit=walk.parseCommit(versionId);
            List<DiffEntry> diffFix=getChangedFileList(verCommit,repo);

            for (DiffEntry entry : diffFix) {
                count ++;
//                bugFiles.add("/Users/lifeasarain/IdeaProjects/druid/"+entry.getNewPath());
                bugFiles.add(repoPath+"/"+entry.getNewPath());
//                bugFiles.add(entry.getNewPath());
            }
            System.out.println(bugFiles);
//			RevWalk walk2 = new RevWalk(repo);
//			ObjectId versionId2=repo.resolve(versionCommit);
//			RevCommit verCommit2=walk2.parseCommit(versionId2);
//			List<DiffEntry> diffFix2=RunJGit.getChangedFileList(verCommit2,repo);
//			for (DiffEntry entry : diffFix2) {
//				System.out.println(entry.getNewPath());
//            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(count);
        return bugFiles;
    }

    public static String getLastCommit(String repoPath){
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.setMustExist(true);
        builder.addCeilingDirectory(new File(repoPath));
        builder.findGitDir(new File(repoPath));
        String lastCommitHash = null;
        try{
            Repository repo = builder.build();
            RevCommit lastCommit = new Git(repo).log().setMaxCount(1).call().iterator().next();
            lastCommitHash = lastCommit.getName();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoHeadException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GitAPIException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lastCommitHash;
    }

    public void cloneRepo(String projectGitUrl, String projectGitPath) {

        try {
            // prepare a new folder for the cloned repository
            File localPath = new File(projectGitPath);
            System.out.println(localPath);
            if (!localPath.exists() && !localPath.isDirectory()) {
                localPath.mkdir();
            }
            localPath.delete();

            // set github username and password
            Properties props = new Properties();
            props.load(this.getClass().getResourceAsStream("/config.properties"));
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            // then clone .setBranchesToClone(Arrays.asList("refs/heads/master"))
            System.out.println("Cloning from " + projectGitUrl + " to " + localPath);
            CloneCommand clone = Git.cloneRepository().setURI(projectGitUrl).setDirectory(localPath);
            UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(username, password);
            clone.setCredentialsProvider(user);

            Git repo1 = clone.call();

            for (Ref b : repo1.branchList().setListMode(ListBranchCommand.ListMode.ALL).call())
                System.out.println("(standard): cloned branch " + b.getName());
            repo1.close();
            // now open the created repository
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(localPath + "/.git")).readEnvironment()
                    // scan environment GIT_DIR
                    // GIT_WORK_TREE
                    // variables
                    .findGitDir() // scan up the file system tree
                    .build();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoHeadException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GitAPIException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        GitUtil gitUtil = new GitUtil();
//        gitUtil.cloneRepo("https://github.com/fastnlp/fitlog", "/Users/lifeasarain/Desktop/SE/plugin/newProject/test");
        System.out.print(getLastCommit("/Users/lifeasarain/IdeaProjects/druid"));
    }

//    public static void main(String[] args){
//        int count = 0;
//        // TODO Auto-generated method stub
//        String versionCommit="010744ef9c441ef98cbd2805f10be63cca8cdbca";//需要分析的Commit Hash
//        String path="/Users/lifeasarain/Desktop/SE/plugin/JIT-defect-prediction and localization/data/deeplearning4j";//对应项目在本地Repo的路径
//        FileRepositoryBuilder builder = new FileRepositoryBuilder();
//        builder.setMustExist(true);
//        builder.addCeilingDirectory(new File(path));
//        builder.findGitDir(new File(path));
//
//        Repository repo;
//        try {
//            repo = builder.build();
//            RevWalk walk = new RevWalk(repo);
//            ObjectId versionId=repo.resolve(versionCommit);
//            RevCommit verCommit=walk.parseCommit(versionId);
//            List<DiffEntry> diffFix=getChangedFileList(verCommit,repo);
//
//            for (DiffEntry entry : diffFix) {
//                count ++;
//                System.out.println(entry.getNewPath());
//            }
////			RevWalk walk2 = new RevWalk(repo);
////			ObjectId versionId2=repo.resolve(versionCommit);
////			RevCommit verCommit2=walk2.parseCommit(versionId2);
////			List<DiffEntry> diffFix2=RunJGit.getChangedFileList(verCommit2,repo);
////			for (DiffEntry entry : diffFix2) {
////				System.out.println(entry.getNewPath());
////            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println(count);
//        String commitHash = "010744ef9c441ef98cbd2805f10be63cca8cdbca";//需要分析的Commit Hash
//        String repoPath = "/Users/lifeasarain/Desktop/SE/plugin/JIT-defect-prediction_and_localization/data/deeplearning4j";//对应项目在本地Repo的路径
//        getBugFilePath(repoPath, commitHash);


}
