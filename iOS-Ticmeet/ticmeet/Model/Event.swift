//
//  Event.swift
//  ticmeet
//
//  Created by Yakup on 16.03.2023.
//

import Foundation
import UIKit

class Event
{
    var eventID = String()
    var eventCategory = String()
    var eventName = String()
    var eventSubscriber = Int()
    var eventDetail = String()
    var eventImage = String()
    var eventLocation = String()
    var eventLongitude = Double()
    var eventLatitude = Double()
    var eventComments = [String]()
    var eventAttented = Int()
    var eventUsersEmail = [String]()
    var eventCurrentUserIsAttented = Bool()
    
    init(eventID: String = String(), eventCategory: String = String(), eventName: String = String(), eventSubscriber: Int = Int(), eventDetail: String = String(), eventImage: String = String(), eventLocation: String = String(), eventLongitude: Double = Double(), eventLatitude: Double = Double(), eventComments: [String] = [String](), eventAttented: Int = Int(), eventUsersEmail: [String] = [String](), eventCurrentUserIsAttented: Bool = Bool()) {
        self.eventID = eventID
        self.eventCategory = eventCategory
        self.eventName = eventName
        self.eventSubscriber = eventSubscriber
        self.eventDetail = eventDetail
        self.eventImage = eventImage
        self.eventLocation = eventLocation
        self.eventLongitude = eventLongitude
        self.eventLatitude = eventLatitude
        self.eventComments = eventComments
        self.eventAttented = eventAttented
        self.eventUsersEmail = eventUsersEmail
        self.eventCurrentUserIsAttented = eventCurrentUserIsAttented
    }
}
