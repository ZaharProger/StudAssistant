package com.example.studassistant.managers;

import android.content.Intent;
import android.net.Uri;

public class EmailManager{
    private static final String[] RECIPIENT = new String[]{"domolegozahar@gmail.com"};
    private static final String SUBJECT = "Приложение Студ Ассистент: отзывы и пожелания";
    private Intent preparedEmail;

    public EmailManager(){
        preparedEmail = new Intent(Intent.ACTION_SENDTO);
        preparedEmail.setData(Uri.parse("mailto:"));
    }

    public Intent prepareMessage(String message){
        if (preparedEmail.hasExtra(Intent.EXTRA_EMAIL))
            preparedEmail.removeExtra(Intent.EXTRA_EMAIL);

        if (preparedEmail.hasExtra(Intent.EXTRA_SUBJECT))
            preparedEmail.removeExtra(Intent.EXTRA_SUBJECT);

        if (preparedEmail.hasExtra(Intent.EXTRA_TEXT))
            preparedEmail.removeExtra(Intent.EXTRA_TEXT);

        preparedEmail.putExtra(Intent.EXTRA_EMAIL, RECIPIENT);
        preparedEmail.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        preparedEmail.putExtra(Intent.EXTRA_TEXT, message);

        return preparedEmail;
    }
}
