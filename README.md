SimpleTaskList
==============

SimpleTaskList is a graphical manager and editor for task lists which use the [todo.txt format](http://todotxt.com). It is easy to use, but still fully featured:

* Comfortable creation and editing of tasks 
* Filter displayed tasks by context / project / due date
* Move completed tasks to a done.txt file
* Save the task list automatically
* Multilingual user interface
* ...

![The main window of SimpleTaskList](https://tortlepp.github.io/SimpleTaskList/SimpleTaskList.png)


Download
========
The most recent version of SimpleTaskList is version 1.0 beta. You can download it here: https://github.com/tortlepp/SimpleTaskList/releases


FAQ
===

How to load a todo.txt file when SimpleTaskList starts?
-------------------------------------------------------
There are two ways to load a file when the application is started: In the settings dialog you can set a *standard task list*. This list will be loaded when the application starts. You can also use the command line an pass the filename to SimpleTaskList: `java -jar SimpleTaskList.jar /home/user/todo.txt`.

How to change the user interface to English / French / ... ?
------------------------------------------------------------
At the moment there are translations for English and German. SimpleTaskList detects the language of your operating system. If it is set to German you will get the German captions, otherwise you will get the English captions. If you want to force the use of the English translation, you can start SimpleTaskList from the command line using this command: `java -Duser.language=en -jar SimpleTaskList.jar` (use `de` to force the German translation).

Why does SimpleTaskList not launch on Linux?
--------------------------------------------
JavaFX was used to create the user interface of SimpleTaskList. Unfortunately this feature is not included in the OpenJDK, which is usually installed when you use the package manager of you distribution. Please install and activate the original [Oracle Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) instead.


Development & Contributions
===========================
SimpleTaskList is entirely written in Java, the user interface was created with JavaFX. The project does not use any third-party libraries. Gradle is used to build the application (Apache Ant was used before - that is why the project structure may look unfamiliar).

At the moment SimpleTaskList is considered as "feature completed". That means all important features are implemented and there are no known bugs. There is no active development of new features right now. But if you find a bug or have an idea for a new cool feature, feel free to create an issue and then I will see how to deal with it.


License
=======

SimpleTaskList is developed and distributed under the terms of the MIT License. See LICENSE for more details.

All icons in SimpleTaskList are taken from [Font Awesome](http://fontawesome.io) by Dave Gandy.
