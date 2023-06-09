//
//  User.swift
//  ticmeet
//
//  Created by Yakup on 16.03.2023.
//

import Foundation
import UIKit

class User
{
    var userName : String?
    var userUsername : String?
    var userEmail : String?
    var userPassword : String?
    var userImage : String?
    var userTopImage : String?
    var userAge : Int?
    var userGender : String?
    var userBio : String?
    var userLocation : String?
    var userFollowers : [String]?
    var userFollowing : [String]?
    var userRegisterDate : Date?
    var userEventsID : [String]?
    
    init(userName: String? = nil, userUsername: String? = nil, userEmail: String? = nil, userPassword: String? = nil, userImage: String? = nil, userTopImage: String? = nil, userAge: Int? = nil, userGender: String? = nil, userBio: String? = nil, userLocation: String? = nil, userFollowers: [String]? = nil, userFollowing: [String]? = nil, userRegisterDate: Date? = nil, userEventsID: [String]? = nil) {
        self.userName = userName
        self.userUsername = userUsername
        self.userEmail = userEmail
        self.userPassword = userPassword
        self.userImage = userImage
        self.userTopImage = userTopImage
        self.userAge = userAge
        self.userGender = userGender
        self.userBio = userBio
        self.userLocation = userLocation
        self.userFollowers = userFollowers
        self.userFollowing = userFollowing
        self.userRegisterDate = userRegisterDate
        self.userEventsID = userEventsID
    }
}


