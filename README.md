# Plugin for JRM

Run sequence
1. build 
2. wait when idea download test environment(IDEA, version see in build.gradle, section "intellij") 
2. gradle -> tasks -> intellij -> runIde
3. when IDEA open test idea: view-> appearance -> toolbar

If you see the JavaRoadMap button in toolbar - all work properly.

ps. for local run, when you wanna use local server - use environment variable local="something"