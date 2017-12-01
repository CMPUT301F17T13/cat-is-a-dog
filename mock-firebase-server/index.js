var FirebaseServer = require('firebase-server');

new FirebaseServer(5000, 'test.firebaseio.com', {
    states: {
        CA: 'California',
        AL: 'Alabama',
        KY: 'Kentucky'
    }
});