# StepsProgressView

Library that show horizontal progress similar to Instagram and Whatsapp.

![Exemplo](https://github.com/junio01/StepsProgressView/blob/master/teste.gif)

## How to Use
```
<com.junio.library.ProgressBarSteps
        android:id="@+id/progressbarSteps"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginStart="5dp"
        android:layout_marginTop="4dp"
        android:layout_marginEnd="5dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:pbsBackgroundColor="@android:color/darker_gray"
        app:pbsForegroundColor="@android:color/white"
        app:pbsStepsCount="4" />       
```    

``` 
progressbarSteps.startProgress()
```

### Optional
CallBacks change Step and Finish Steps
```
progressbarSteps.setListener(this)
```

### Actions
Click Left View, Back Step

Click Right View, Skip Step

Keep the screen pressed, Pause Step
