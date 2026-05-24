# Android Task Hijacking – Assignment 3 (SMD)

## Overview

This project demonstrates the Android Task Hijacking vulnerability (also known as StrandHogg-style behavior) using two Android applications:

- **Victim app** – a simple “secure” banking-style application.
- **Attacker app** – a malicious application that displays a fake login screen.

The purpose of the project is educational: to explain how Android task management mechanisms such as Activities, Tasks, Back Stack, Intents, and Manifest configuration can be abused on vulnerable Android versions.

# General Idea

To understand this vulnerability, we first need to understand that Android works with components such as:

- Activity
- Task
- Back Stack
- Intent
- Manifest configuration

An **Activity** represents one screen of an Android application.

Android groups activities into **tasks**. These tasks appear in the Recent Apps screen and are restored when the user resumes an application.

A **task** is a collection of activities arranged in a **back stack**, where the last opened activity is usually the first displayed to the user.

This vulnerability appears when a malicious application manages to place one of its activities inside the same task context as a legitimate victim application.

The user believes they opened the legitimate application, but the first screen displayed may actually belong to the attacker application. This creates a UI deception problem because the user trusts the visual context of the application.

As a note, the original StrandHogg 1.0 task-affinity vulnerability was mitigated by platform patches, and Android SDK/API 30 (Android 11) and newer versions contain protections against this class of vulnerability.

---

# Technical View and Planning

The project is based on two Android applications:

## 1. Victim Application

A simple banking-style application that acts as the legitimate target.

### Victim App Configuration

#### Package Name

```text
com.demo.victim
```

This package name is important because the attacker application needs to know which real application should be opened after the fake login screen closes.

### AndroidManifest.xml Configuration

#### Exported Activity

```xml
android:exported="true"
```

This allows the activity to be launched externally through an Intent.

For launcher activities, this is also required on modern Android versions.

#### Task Affinity

```xml
android:taskAffinity="com.demo.victim"
```

This tells Android that the activity prefers to belong to a task with this affinity.

Normally, task affinity is based on the application package name. The security issue appears because another application can declare the same task affinity on vulnerable Android versions.

---

## 2. Attacker Application

A malicious educational application that mimics the victim application.

### Attacker App Configuration

#### Package Name

```text
com.demo.attacker
```

#### Target Victim Package

```text
com.demo.victim
```

The attacker application contains a fake login UI designed to imitate the victim application.

In a real-world attack, this interface could be used to steal credentials or other sensitive information.

---

# Attack Flow

## Move Task to Background

```java
if (savedInstanceState == null) {
    moveTaskToBack(true);
}
```

This moves the attacker task into the background during the first execution.

The goal is to make the attack flow appear more realistic and reduce user suspicion.

---

## Launch Victim Application

```java
Intent launchIntent =
    getPackageManager().getLaunchIntentForPackage(VICTIM_PACKAGE);
```

This retrieves the normal launch Intent for the victim application.

The attacker launches the legitimate application and closes the fake login activity.

---

# Attacker Manifest Configuration

## Shared Task Affinity

```xml
android:taskAffinity="com.demo.victim"
```

This allows the attacker activity to associate with the same task affinity as the victim application.

---

## Allow Task Reparenting

```xml
android:allowTaskReparenting="true"
```

This helps the attacker activity behave more predictably inside the Android task stack.

---

## Hide from Recent Apps

```xml
android:excludeFromRecents="true"
```

This hides the attacker task from the Recent Apps screen, making the malicious application less visible to the user.

---

# Flow Diagram

The project includes a flow diagram describing:

1. Launching the attacker application
2. Moving the attacker task to background
3. Launching the victim application
4. Injecting attacker UI into the victim task
5. User interaction with fake login screen
6. Returning control to the legitimate application

---

# Architectural Diagram

The architectural diagram contains:

- Victim application
- Attacker application
- Android Task Manager
- Activities
- Task affinity relationship
- Intent communication flow

---

# References:
- Android Tasks and Back Stack  
  https://developer.android.com/guide/components/activities/tasks-and-back-stack

- Android StrandHogg Documentation  
  https://developer.android.com/privacy-and-security/risks/strandhogg
