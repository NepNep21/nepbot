Nepbot is the first project I ever made, so be prepared for shitcode!

# Building

Just run "./gradlew build", the resulting binary will be in build/libs/ and will contain all libraries

# Using

This bot uses 2 environment variables: 

BOT_TOKEN: Your bots complete token  
MONGODB_URL: The URL the bot will use to connect to a mongodb database

Setting environment variables is operating system dependent, so search how to do it

Your database should be called Nepbot and have 2 collections, one called "Guilds", and one called "Misc"

Due to my shitcode, you have to initialize the "Misc" collection with a document, the structure should be this:

![Document](https://github.com/NepNep21/nepbot/blob/main/.github/document.png?raw=true)

Create a file in the same directory as the bot, and create a file called "config.json", it should look something like this

```json
{
  "operator": 12345,
  "reconnectDelay": 32,
  "activity": {
    "type": "watching",
    "content": "Nepbot be made"
  },
  "uncalledMessages": false
}
```

Where:  
"operator" is your discord id  
"reconnectDelay" is the maximum number of seconds you want JDA to wait before attempting a reconnect (min 32)  
"type" is the type of activity ("watching", "playing", "competing", or "listening")  
"content" is the content of the activity  
"uncalledMessages" is whether actions in "src/main/kotlin/me/nepnep/nepbot/message/Messages.kt" should be executed

Make sure you have java installed, and then run the bot with "java -jar fileName.jar"
