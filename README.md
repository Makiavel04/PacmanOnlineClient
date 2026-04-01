## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
# PacmanOnlineClient
# On crée un dossier temporaire
mkdir -p build_tmp/lib_classes
# On extrait les librairies dedans
cd build_tmp/lib_classes
jar xf ../../lib/json-20231013.jar
jar xf ../../lib/pacman-online-commun.jar
cd ../..
# On crée le Fat JAR contenant ton code + le code des libs
jar cvfe ClientPacmanComplet.jar ClientPacman -C bin . -C build_tmp/lib_classes .

# créer stub.sh
#!/bin/sh
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
exec java -jar "$MYSELF" "$@"
exit 1

# fusionne
cat stub.sh ClientPacmanComplet.jar > pacman-game
chmod +x pacman-game