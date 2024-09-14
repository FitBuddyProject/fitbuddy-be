db.User.insert(
        {
            "_id": {"$oid": "f3227ff42e2e062697ce466c"},
            "_class": "com.fitbuddy.service.repository.entity.User",
            "buddies": ["{ \"$ref\" : \"MyBuddy\", \"$id\" : \"819bfd2a9cc9982b5d562889\", \"$db\" : \"MyBuddy\" }", "{ \"$ref\" : \"MyBuddy\", \"$id\" : \"02937bb5b74d95fc6e642ecd\", \"$db\" : \"MyBuddy\" }"],
            "joinDate": {"$date": "2024-09-01T06:47:09.362Z"},
            "lastModifiedDate": {"$date": "2024-09-01T06:47:09.362Z"},
            "nickname": "shbabo",
            "password": "$2a$10$L27MB1mFmwr6oTa6UHt9JOetNvkQohVuCAcJrw/V6/VeQgRsYvVIm",
            "phone": "112112112",
            "refreshToken": "eyJ0b2tlblR5cGUiOiJSRUZSRVNIIiwiYWxnIjoiSFMyNTYifQ.eyJCT0RZIjoie1wiam9pbkRhdGVcIjpcIjIwMjQtMDktMDFUMTU6NDc6MDkuMjQxMjIzXCIsXCJ1dWlkXCI6XCJmMzIyN2ZmNDJlMmUwNjI2OTdjZTQ2NmNcIixcImlkXCI6XCIxMTIxMTIxMTJcIixcImxhc3RNb2RpZmllZERhdGVcIjpcIjIwMjQtMDktMDFUMTU6NDc6MDkuMjQxMjIzXCJ9IiwiaXNzIjoiZml0QnVkZHkiLCJqdGkiOiJmMzIyN2ZmNDJlMmUwNjI2OTdjZTQ2NmMiLCJleHAiOjE3MjUxNzMyMjksIm5iZiI6MTcyNTE3MzIyOSwiaWF0IjoxNzI1Nzc4MDI5fQ.vh5djATMoEJr6lx1StDqOJcDZOXc_7SB1mmVoUajuGo",
            "sendable": false,
            "tired": 0
        }
);

db.MyBuddy.insert(
        {
            "_id": {"$oid": "02937bb5b74d95fc6e642ecd"},
            "User_id": "f3227ff42e2e062697ce466c",
            "_class": "com.fitbuddy.service.repository.entity.MyBuddy",
            "buddy": "DUCK",
            "exp": 0,
            "isPrimary": true,
            "name": "도널드"
        }
);