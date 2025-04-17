@echo off
:: Установите путь к JDK и JavaFX SDK
set JAVA_HOME=C:\path\to\jdk-20
set PATH=%JAVA_HOME%\bin;%PATH%
set JAVAFX_HOME=./javafx-sdk-24

:: Запустите приложение
java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -jar TetraederFX.jar

:: Пауза для просмотра ошибок (если они есть)
pause