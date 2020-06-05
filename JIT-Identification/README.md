# Just-in-Time-Defect-Prediction-Features
This repository is an accompanying repository for our paper "A Large-Scale Empirical Study of Applying Just-in-Time Defect Prediction at Industry A Case Study at Alibaba"


## Running Environment
Linux or Mac
### Software
* python3
* mysql
* git

### Python Packages
* sqlalchemy
* simplejson
* mysql-connector


## Configuration File
* Configuration file path: defect_features/config.json
* Configurations
   1. data_root_path: project data path
   2. projects: project names
   3. git_log_path: data about a change (including metadata, namestat, and numstat of a change) need to be recorded in three files in reverse order using the command `git log`. In this path, we store the files.
   4. sql: configurations of mysql. dbname is the name of the database when running our program.
   5. extensions: The studied file extensions.

## Before Running
Prepare your studied projects in the path `data_root_path`. Make sure that the projects are using git to manage their code.

## Steps
1. Gnerating git logs: run `defect_features/log_generation.py`. 
2. Calculating features: run `defect_features/main.py`
3. Combining features: run `defect_features/utils/features_combination.py`

