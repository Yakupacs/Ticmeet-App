//
//  adminVC.swift
//  ticmeet
//
//  Created by Yakup on 20.03.2023.
//

import UIKit
import MapKit
import FirebaseAuth
import FirebaseStorage
import FirebaseFirestore
import CoreLocation

class adminVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, MKMapViewDelegate, CLLocationManagerDelegate {

    @IBOutlet weak var eventNameLabel: UITextField!
    @IBOutlet weak var eventDetailsLabel: UITextField!
    @IBOutlet weak var eventImageView: UIImageView!
    @IBOutlet weak var eventLocationLabel: UITextField!
    @IBOutlet weak var eventMapView: MKMapView!
    @IBOutlet weak var logoutButton: UIButton!
    @IBOutlet weak var eventPopUpButton: UIButton!
    
    var selectedLatitude = 0.0
    var selectedLongitude = 0.0
    
    var locationManager = CLLocationManager()
    
    var selectedCategory = String()
    
    var event = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setPopUpButton()
        
        eventMapView.delegate = self
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()

        eventImageView.isUserInteractionEnabled = true
        let imageGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(chooseImage))
        eventImageView.addGestureRecognizer(imageGestureRecognizer)
        
        let mapGestureRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(selectLocation(gestureRecognizer:)))
        mapGestureRecognizer.minimumPressDuration = 1
        eventMapView.addGestureRecognizer(mapGestureRecognizer)
    }
    
    func setPopUpButton(){
        let optionClosure = {(action: UIAction) in
            self.event.eventCategory = action.title
        }
        
        eventPopUpButton.menu = UIMenu(children: [UIAction(title: "Konser",  handler: optionClosure),
                                                UIAction(title: "Tiyatro",  handler: optionClosure),
                                                UIAction(title: "Sinema",  handler: optionClosure),
                                                UIAction(title: "Söyleşi",  handler: optionClosure),
                                                UIAction(title: "Sergi",  handler: optionClosure),
                                                UIAction(title: "Opera", handler: optionClosure),
                                                UIAction(title: "Performans", handler: optionClosure),
                                                UIAction(title: "Bale", handler: optionClosure)])
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation is MKUserLocation{
            return nil
        }
        
        let reuseId = "myAnnotation"
        var pinView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseId)
        
        if pinView == nil{
            pinView = MKMarkerAnnotationView(annotation: annotation, reuseIdentifier: reuseId)
            pinView?.canShowCallout = true
            pinView?.tintColor = .black
            let button = UIButton(type: .detailDisclosure)
            pinView?.rightCalloutAccessoryView = button
        }
        else{
            pinView?.annotation = annotation
        }
        
        return pinView
        
    }
    
    @objc func chooseImage(){
        let pickerController = UIImagePickerController()
        pickerController.delegate = self
        pickerController.sourceType = .photoLibrary
        present(pickerController, animated: true)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        eventImageView.image = info[.originalImage] as? UIImage
        self.dismiss(animated: true)
    }
    
    @objc func selectLocation(gestureRecognizer : UILongPressGestureRecognizer){
        if gestureRecognizer.state == .began && selectedLatitude == 0.0 && selectedLongitude == 0.0{
            let touchPoint = gestureRecognizer.location(in: eventMapView)
            let touchCoordinat = eventMapView.convert(touchPoint, toCoordinateFrom: eventMapView)
                
            event.eventLatitude = touchCoordinat.latitude
            event.eventLongitude = touchCoordinat.longitude
                
            let annotation = MKPointAnnotation()
            annotation.coordinate = touchCoordinat
            eventMapView.addAnnotation(annotation)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location = CLLocationCoordinate2DMake(locations[0].coordinate.latitude, locations[0].coordinate.longitude)
            
        let span = MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
        let region = MKCoordinateRegion(center: location, span: span)
        eventMapView.setRegion(region, animated: true)
    }

    @IBAction func addActivityFunc(_ sender: Any) {
        if eventNameLabel.text == "" || eventDetailsLabel.text == "" || eventLocationLabel.text == ""{
            print("eksik satırlar var")
            return
        }
        else{
            event.eventName = eventNameLabel.text!
            event.eventDetail = eventDetailsLabel.text!
            event.eventLocation = eventLocationLabel.text!
        }
        let storage = Storage.storage()
        let storageReference = storage.reference()
        
        let mediaFolder = storageReference.child("eventImages")
        
        if let data = eventImageView.image?.jpegData(compressionQuality: 0.5)
        {
            let uuid = UUID().uuidString
            
            let imageReference = mediaFolder.child("\(uuid).jpg")
            
            imageReference.putData(data,metadata: nil) { metadata, error in
                if error != nil{
                    print(error!.localizedDescription)
                }
                else{
                    imageReference.downloadURL { url, error in
                        if error == nil{
                            self.event.eventImage = url!.absoluteString
                            
                            let firestoreDatabase = Firestore.firestore()
                            
                            var firestoreReference : DocumentReference? = nil
                            
                            let firestorePost = [
                                                "eventId" : UUID().uuidString,
                                                "eventCategory" : self.selectedCategory,
                                                 "eventName" : self.event.eventName,
                                                 "eventDetail" : self.event.eventDetail,
                                                 "eventImage" : self.event.eventImage,
                                                 "eventLocation" : self.event.eventLocation,
                                                 "eventLongitude" : self.event.eventLongitude,
                                                 "eventLatitude" : self.event.eventLatitude,
                                                 "eventAttented" : 0,
                                                 "eventComments" : [],
                                                 "eventUsersID" : [],
                                                 "postedBy" : Auth.auth().currentUser!.email!] as [String : Any]
                            
                            firestoreReference = firestoreDatabase.collection("Event").addDocument(data: firestorePost, completion: { error in
                                if error != nil{
                                    print(error!.localizedDescription)
                                }
                                else{
                                    print("Veri kaydetme başarılı.")
                                }
                            })
                        }
                    }
                }
            }
        }
    }
    
    @IBAction func logoutFunc(_ sender: Any) {
        do{
            try Auth.auth().signOut()
            event = Event()
            performSegue(withIdentifier: "toLogin", sender: nil)
        }catch{
            print(error.localizedDescription)
        }
    }
}
