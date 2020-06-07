import sys
import os


# from defect_features.log_generation import GitLog




def runmodel(commithash, pythonProjectPath):

    os.chdir(pythonProjectPath)
    sys.path.append(pythonProjectPath)

    # print(commithash)

    from defect_features.config import conf
    from defect_features.onelog_generation import GitOneLog
    from defect_features.onelog_feature import OneLogFeatures
    from defect_features.utils.features_combination import FeatureCombination
    from defect_features.idmodel import Idmodel

    for p in conf.projects:
        # print('Project',p)
        GitOneLog().commitrun(p)
        OneLogFeatures().log_one_feature()
        FeatureCombination().one_featureCombination(commithash)

        result = Idmodel().runIdmodel(pythonProjectPath)
        if(1 == result):
            print('1')
            return '1'
        else:
            print('0')
            return '0'

if __name__ == '__main__':
     runmodel(sys.argv[1], sys.argv[2])
    # runmodel("a7844ab716eeeaddad24061dcf6224264444c67b", "/Users/lifeasarain/Desktop/JITO/JIT-Identification")