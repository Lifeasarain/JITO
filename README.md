# JITO

JITO is an Intellij IDEA plugin used to identify defect and locate buggy lines.



## Installation

1. Download  `JITO-Client.zip`;
2. Open Intellij IDEA, choose 
   - Windows：`File` -> `Settings` -> `Plugins`;

   - Mac：`IntelliJ IDEA` -> `Preferences` -> `Plugins`;
3. choose `Install Plugin from Disk`;
4. find the path of the jar and install it.
5. Restart Intellij IDEA to activate the plugin.



## Usage

1. When using the tool for the first time, choose `Tools` -> `JITO`-> `Build model`;
2. Analyze models will be established;
3. When submitting a new change commit to local git repository, choose `Tools` -> `JITO`-> `Analyze changes`;
4. If it identified as bug change, buggy lines will be highlighted, else return no bug information;
5. If want remove highlight, choose `Tools` -> `JITO`-> `Unhighlight`;


