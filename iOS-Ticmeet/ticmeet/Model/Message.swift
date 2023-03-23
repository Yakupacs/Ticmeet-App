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
    var messageIsSeen = Bool()
    var messageUserImage = UIImage()
    var messageTime = Date()
    
    init(message: String = String(), messageUserEmail: String = String(), messageUserName: String = String(), messageIsSeen: Bool = Bool(), messageUserImage: UIImage = UIImage(), messageTime: Date = Date()) {
        self.message = message
        self.messageUserEmail = messageUserEmail
        self.messageUserName = messageUserName
        self.messageIsSeen = messageIsSeen
        self.messageUserImage = messageUserImage
        self.messageTime = messageTime
    }
}
