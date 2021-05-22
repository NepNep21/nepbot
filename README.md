Nepbot is the first project i ever made, so be prepared for shitcode!

#Building

Just run "./gradlew build", the resulting binary will be in build/libs/ and will contain all libraries

#Using

This bot uses 2 environment variables: 

BOT_TOKEN: Your bot's complete token  
MONGODB_URL: The URL the bot will use to connect to a mongodb database

Setting environment variables is operating system dependent, so search how to do it

Your database should be called Nepbot and have 2 collections, one called "Guilds", and one called "Misc"

Due to my shitcode, you have to initialize the "Misc" collection with a document, the structure should be this:

![Document](https://github.com/Lolicon2110/Nepbot/blob/main/.github/document.png?raw=true)

Make sure you have java installed, and then run the bot with "java -jar fileName.jar"