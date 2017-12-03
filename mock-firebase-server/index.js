var FirebaseServer = require('firebase-server');

new FirebaseServer(5000, 'test.firebaseio.com', {
	"habits": {
		"mockUserId": {
			"1": {
				"reason": "Test Reason",
				"schedule": [true,true,true,true,true,true,true],
				"startDate": 1512198000000,
				"title": "Test Habit",
				"userId": "mockUserId",
				"key": "1"
			}
		}
	},
	states: {
			CA: 'California',
			AL: 'Alabama',
			KY: 'Kentucky'
	}
});