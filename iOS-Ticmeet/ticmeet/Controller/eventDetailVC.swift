//
//  eventDetailVC.swift
//  ticmeet
//
//  Created by Yakup on 23.03.2023.
//

import UIKit

class eventDetailVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var usersCollectionView: UICollectionView!
    @IBOutlet weak var eventLocationLabel: UILabel!
    @IBOutlet weak var eventAttentedLabel: UILabel!
    @IBOutlet weak var eventDetailLabel: UILabel!
    @IBOutlet weak var eventImageView: UIImageView!
    
    @IBOutlet weak var attentedView: UIView!
    @IBOutlet weak var locationView: UIView!
    @IBOutlet weak var detailView: UIView!
    @IBOutlet weak var eventUsersView: UIView!
    
    var getEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        usersCollectionView.delegate = self
        usersCollectionView.dataSource = self
        setConfigure()
        
        let layout = usersCollectionView.collectionViewLayout as? UICollectionViewFlowLayout
        layout?.minimumLineSpacing = -5
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as? eventUsersCell{
            cell.userImageView.image = UIImage(named: "yakup")
            cell.userImageView.layer.cornerRadius = 20
            cell.userImageView.layer.borderWidth = 1
            cell.userImageView.layer.borderColor = UIColor.black.cgColor
            return cell
        }
        
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 15
    }
    
    
    func setConfigure(){
        eventNameLabel.text = getEvent.eventName
        eventLocationLabel.text = getEvent.eventLocation
        eventDetailLabel.text = getEvent.eventDetail
        eventAttentedLabel.text = "\(getEvent.eventAttented)"
        
        eventImageView.layer.cornerRadius = 25
        detailView.layer.cornerRadius = 15
        attentedView.layer.cornerRadius = 15
        locationView.layer.cornerRadius = 15
        eventUsersView.layer.cornerRadius = 15
    }

    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
}
