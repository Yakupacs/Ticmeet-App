//
//  Message.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import Foundation
import UIKit

class Message{
    var message = String()
    var messageUserEmail = String()
    var messageUserName = String()
    var messageUserUsername = String()
    var messageIsSeen = Bool()
    var messageUserImage = String()
    var messageTime = Date()
    

    init(message: String = String(), messageUserEmail: String = String(), messageUserName: String = String(), messageUserUsername: String = String(), messageIsSeen: Bool = Bool(), messageUserImage: String = String(), messageTime: Date = Date()) {
        self.message = message
        self.messageUserEmail = messageUserEmail
        self.messageUserName = messageUserName
        self.messageUserUsername = messageUserUsername
        self.messageIsSeen = messageIsSeen
        self.messageUserImage = messageUserImage
        self.messageTime = messageTime
    }
}
