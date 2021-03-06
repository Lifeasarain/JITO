import sys
import os
import json



def buildmodel(projectName, pythonProjectPath, dataPath, start_time, end_time):

    dict_var = {
        "data_root_path": dataPath,
        "projects": [projectName],
        "df_basic_path": pythonProjectPath+"/data/df/",
        "git_log_path": pythonProjectPath+"/data/log/",
        "local_path": pythonProjectPath+"/",
        "extensions": [".py",".cc",".cpp",".java"],
    }
    os.chdir(pythonProjectPath)
    sys.path.append(pythonProjectPath)

    with open("defect_features/config.json", "w", encoding='utf-8') as f:
        json.dump(dict_var, f, indent=2, sort_keys=True, ensure_ascii=False)


    from defect_features.log_generation import GitLog
    from defect_features.config import conf
    from defect_features.log_features import LogFeatures
    from defect_features.utils.features_combination import FeatureCombination
    from defect_features.idmodel import Idmodel

    for p in conf.projects:
        # print('Project', p)
        GitLog().run(p, end_time)
        LogFeatures().log_feature(start_time)
        FeatureCombination().featureCombination()
        Idmodel().buildIdmodel(pythonProjectPath)
        print('Model Build Success')

if __name__ == '__main__':
    buildmodel(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
    # buildmodel("druid", "/Users/lifeasarain/Desktop/JITO/JIT-Identification", "/Users/lifeasarain/IdeaProjects/")
    # buildmodel("druid", "/Users/lifeasarain/Desktop/tmp/JITO/JITO-Identification", "/Users/lifeasarain/IdeaProjects/", "2014-1-1", "2015-1-1")

