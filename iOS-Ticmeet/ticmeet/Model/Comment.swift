//
//  Comment.swift
//  ticmeet
//
//  Created by Yakup on 22.03.2023.
//

import Foundation
import UIKit

class Comment{
    var commentEventID : String?
    var comment : String?
    var commentUserName : String?
    var commentUserUsername : String?

    init(commentEventID: String? = nil, comment: String? = nil, commentUserName: String? = nil, commentUserUsername: String? = nil) {
        self.commentEventID = commentEventID
        self.comment = comment
        self.commentUserName = commentUserName
        self.commentUserUsername = commentUserUsername
    }
}
