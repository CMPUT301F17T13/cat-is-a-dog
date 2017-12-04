var FirebaseServer = require('firebase-server');

var server = new FirebaseServer(5000, 'test.firebaseio.com', {
	"events": {
		"mockUserId": {
			"1": {
				"comment": "Did it",
				"complete": "yes",
				"eventDate": 1512199000000,
				"habitKey":"1",
				"habitStamp":"sample_stamp",
				"key":"1",
				"latitude":1,
				"longitude":1,
				"user":"mockUserId"
			}
		}
	},
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
	"followrequests": {
		"-L-StJnMIpkVTi-lf9h2" : {
			"accepted" : true,
			"followee" : "kevinId",
			"follower" : "mockUserId",
			"key" : "-L-StJnMIpkVTi-lf9h2",
			"requestTimestamp": null
		},
		"-L-StJnMIpkVTi-lf9h3" : {
			"accepted" : true,
			"followee" : "mockUserId",
			"follower" : "jamesId",
			"key" : "-L-StJnMIpkVTi-lf9h3",
			"requestTimestamp": null
		},
		"-L-StJnMIpkVTi-lf9h4" : {
			"accepted" : false,
			"followee" : "mockUserId",
			"follower" : "nathanId",
			"key" : "-L-StJnMIpkVTi-lf9h3",
			"requestTimestamp": null
		}
	},
	"users" : {
		"kevinId" : {
		  "displayName" : "Kevin",
		  "email" : "weixiang@ualberta.ca",
		  "photoUrl" : "https://lh3.googleusercontent.com/-Rd6tMqpUCAY/AAAAAAAAAAI/AAAAAAAAAAo/QE2dEXLgx9s/s96-c/photo.jpg",
		  "userId" : "kevinId"
		},
		"jamesId" : {
		  "displayName" : "James",
		  "email" : "james@ualberta.ca",
		  "photoUrl" : "https://lh3.googleusercontent.com/-Rd6tMqpUCAY/AAAAAAAAAAI/AAAAAAAAAAo/QE2dEXLgx9s/s96-c/photo.jpg",
		  "userId" : "jamesId"
		},
		"nathanId" : {
			"displayName" : "Nathan",
			"email" : "nathan@ualberta.ca",
			"photoUrl" : "https://lh3.googleusercontent.com/-Rd6tMqpUCAY/AAAAAAAAAAI/AAAAAAAAAAo/QE2dEXLgx9s/s96-c/photo.jpg",
			"userId" : "nathanId"
		}
	}
});

server.setRules({
	"rules": {
    ".read": "auth != null",
    ".write": false,
	}
});