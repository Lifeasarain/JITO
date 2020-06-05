# JITO

JITO is an Intellij IDEA plugin used to identify defect and locate buggy lines.



## Installation

1. Download  `JITO-Client.zip` and `JITO-Identification.zip`
2. Unzip `JITO-Identification.zip`
3. Open Intellij IDEA, choose 
   - Windows：`File` -> `Settings` -> `Plugins`;

   - Mac：`IntelliJ IDEA` -> `Preferences` -> `Plugins`;
4. choose `Install Plugin from Disk`;
5. find the path of the `JITO-Client.zip` and install it.
6. Restart Intellij IDEA to activate the plugin.



## Usage

1. When using the tool for the first time, choose `Tools` -> `JITO`-> `Set properties`, set Python Interpreter path and `JITO-Identification` path;
2. At the first time, choose `Tools` -> `JITO`-> `Build model`. Analyze models will be established;
3. When submitting a new change commit to local git repository, choose `Tools` -> `JITO`-> `Analyze changes`;
4. If it identified as bug change, buggy lines will be highlighted, else return no bug information;
5. If want remove highlight, choose `Tools` -> `JITO`-> `Unhighlight`;


