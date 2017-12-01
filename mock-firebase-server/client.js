var firebase = require('firebase');

// Set the configuration for your app
/*var config = {
    apiKey: "apiKey",
    authDomain: "projectId.firebaseapp.com",
    databaseURL: "https://databaseName.firebaseio.com",
    storageBucket: "bucket.appspot.com"
};*/
var config = {
    databaseURL: 'ws://test.firebaseio.com:5000'
};
firebase.initializeApp(config);

// Get a reference to the database service
var database = firebase.database();

database.ref('test/' + 'test').set({
    test: "test"
});