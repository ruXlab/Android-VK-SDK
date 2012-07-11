# VK Android SDK

Библиотека для ваших Android-приложений, обеспечивающая доступ к VK API.

Это - форк проекта Android-VKontakte-SDK(https://github.com/thest1/Android-VKontakte-SDK), основная разработка 
ведётся в репозитарии [android-vk-sdk на bitbucket](https://bitbucket.org/ruX/android-vk-sdk)

Основной задачей этого форка: поддерживать новые Vk API и отформатировать, привести к стандартному java-коду оригинальную библиотеку. 

## Использование
Быстро понять принципы работы библиотеки вам поможет демо-проект AndroidVkSdkSample. В нём реализована авторизация и в качестве примера отправка записи на свою стену.  
Чтобы подключить библиотеку к своему проекту, нужно выполнить следующие шаги:  

1. Импортировать проект AndroidVkSdk в Eclipse. AndroidVkSdk реализован как Android Library Project.  

2. Добавить в своём проекте ссылку на AndroidVkSdk.  

3. Убедиться что у вашего приложения есть разрешение на доступ в Интернет

    ``` xml
    <uses-permission android:name="android.permission.INTERNET"/>  
    ```
  
4. Создать url авторизации и открыть его в WebView

    ``` java
    String url=Auth.getUrl(API_ID, Auth.getSettings());
    webview.loadUrl(url);
    ```
    
    Здесь в качестве API_ID должен быть указан ID вашего приложения, созданного на http://vk.com/developers.php

5. Дождаться когда webview будет перенаправлен на Auth.redirect_url. Распарсить redirect_url чтобы получить из него access_token. Для примера смотрите как это сделано в LoginActivity.java в проекте AndroidVkSdkSample.  

6. Создать объект Api для выполнения запросов к серверу ВКонтакте:

    ``` java
    API api=new Api(access_token, API_ID);  
    ```
    
7. Теперь можно выполнять запросы к серверу, например так:

    ``` java
    api.createWallPost(user_id, text, null, null, false, false, false, null, null);
    ```


## Лицензия

Библиотека распространяется по [лицензии MIT](https://github.com/thest1/Android-VKontakte-SDK/blob/master/LICENSE).
