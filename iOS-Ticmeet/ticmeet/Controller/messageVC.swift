//
//  messageVC.swift
//  ticmeet
//
//  Created by Yakup on 29.03.2023.
//

import UIKit
import Firebase

class messageVC: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var userUsernameLabel: UILabel!
    @IBOutlet weak var messageTextfield: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var messageView: UIView!
    
    var previousLabel: UILabel? = nil
    
    var labels = [UILabel]()
    
    var messages = [Message]()
    var mainUser = User()
    var otherUser = User()
    
    // MARK: - viewDidLoad()
    override func viewDidLoad() {
        super.viewDidLoad()
        setAppearance()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mainUser.userEmail = Auth.auth().currentUser?.email
        print("mainUser: \(mainUser.userEmail!)\notherUser: \(otherUser.userEmail!)")
        getMessages()
    }
    
    @IBAction func sendMessage(_ sender: Any) {
        if messageTextfield.text != ""{
            let firestore = Firestore.firestore()
            
            firestore.collection("Message")
                .whereField("usersEmail", arrayContains: mainUser.userEmail!)
                .getDocuments { querySnapshot, error in
                    if let error = error{
                        print("Hata: \(error.localizedDescription)")
                    }
                    else{
                        guard let querySnapshot = querySnapshot else { return }
                        
                        var sign = 0
                        
                        for document in querySnapshot.documents{
                            
                            if let usersEmail = document.get("usersEmail") as? [String]{
                                
                                if usersEmail.contains(self.otherUser.userEmail!), usersEmail.contains(self.mainUser.userEmail!){
                                    sign = 1
                                    
                                    let documentRef = document.reference
                                    
                                    let newMessage = [
                                        "message": self.messageTextfield.text!,
                                        "timestamp": Date(),
                                        "messageSeen": false,
                                        "messageUserEmail": self.mainUser.userEmail!
                                    ] as [String : Any]
                                    
                                    documentRef.updateData(["messages": FieldValue.arrayUnion([newMessage])]) { error in
                                        if let error = error{
                                            print("Hata: \(error.localizedDescription)")
                                        }
                                        else{
                                            self.messageTextfield.text = ""
                                            print("Veri güncelleme başarılı!")
                                        }
                                    }
                                    
                                }
                                
                            }
                            
                        }
                        
                        if sign == 0{
                            
                            var firestoree = Firestore.firestore()
                            
                            let newMessage = [
                                "message": self.messageTextfield.text!,
                                "timestamp": Date(),
                                "messageSeen": false,
                                "messageUserEmail": self.mainUser.userEmail!
                            ] as [String : Any]
                            
                            
                            firestoree.collection("Message").addDocument(data: ["usersEmail" : [self.mainUser.userEmail!, self.otherUser.userEmail!],"messages": FieldValue.arrayUnion([newMessage])]) { error in
                                if let error = error{
                                    print("Hata: \(error.localizedDescription)")
                                }
                                else{
                                    print("İlk mesaj ekleme başarılı!")
                                }
                            }
                        }
                    }
                }
        }
        
    }
    
    
    // MARK: - getMessages()
    func getMessages(){
        let firestore = Firestore.firestore()
        
        firestore.collection("Message")
            .whereField("usersEmail", arrayContains: mainUser.userEmail!)
            .addSnapshotListener { querySnapshot, error in
                if let error = error{
                    print("Hata: \(error.localizedDescription)")
                }
                else{
                    self.messages = []
                    
                    guard let querySnapshot = querySnapshot else { return }
                    
                    for document in querySnapshot.documents{
                        if let usersEmails = document.get("usersEmail") as? [String]{
                            if usersEmails.contains(self.otherUser.userEmail!), usersEmails.contains(self.mainUser.userEmail!){
                                if let messages = document.get("messages") as? [[String: Any]]{
                                    for message in messages{
                                        let element = Message()
                                        for val in message{
                                            
                                            if val.key == "message"{
                                                element.message = val.value as! String
                                            }
                                            else if val.key == "messageSeen"{
                                                element.messageIsSeen = val.value as! Bool
                                            }
                                            else if val.key == "messageUserEmail"{
                                                element.messageUserEmail = val.value as! String
                                            }
                                            else if val.key == "timestamp" {
                                                if let timestamp = val.value as? Timestamp {
                                                    let seconds = Double(timestamp.seconds)
                                                    let nanoseconds = Double(timestamp.nanoseconds) / 1_000_000_000
                                                    element.messageTime = Date(timeIntervalSince1970: seconds + nanoseconds)
                                                }
                                            }
                                            
                                        }
                                        self.messages.append(element)
                                    }
                                }
                                
                            }
                            
                        }
                    }
                    self.createLabel()
                }
            }
    }
    
    // MARK: - textFieldDidChangeSelection()
    func textFieldDidChangeSelection(_ textField: UITextField) {
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        
    }
    
    // MARK: - keyboardWillShow()
    // Keyboard açıldığında çağrılacak fonksiyon
    @objc func keyboardWillShow(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            // Klavye yüksekliğini al
            let keyboardHeight = keyboardSize.height
            // Mesaj görünümünün yeni konumunu belirle
            let newMessageViewY = self.view.frame.size.height - keyboardHeight - self.messageView.frame.size.height
            // Animasyonlu olarak mesaj görünümünü taşı
            UIView.animate(withDuration: 0.1, animations: {
                self.messageView.frame.origin.y = newMessageViewY
            })
        }
    }
    
    // MARK: - keyboardWillHide
    // Keyboard kapandığında çağrılacak fonksiyon
    @objc func keyboardWillHide(notification: NSNotification) {
        // Mesaj görünümünü animasyonlu olarak orijinal konumuna taşı
        UIView.animate(withDuration: 0.1, animations: { [self] in
            self.messageView.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: 10).isActive = true
        })
    }
    
    // MARK: - textFieldLeftImage
    func textFieldLeftImage(imageName: String, textField: UITextField){
        let imageView = UIImageView(frame: CGRect(x: 8.0, y: 8.0, width: 24.0, height: 24.0))
        let image = UIImage(named: imageName);
        imageView.contentMode = .scaleAspectFit
        imageView.image = image;
        let view = UIView(frame: CGRect(x: 0, y: 0, width: 40, height: 40))
        view.addSubview(imageView)
        
        textField.leftView = view;
        textField.leftViewMode = .always
        textField.layer.cornerRadius = 20.0
        textField.layer.borderWidth = 1.5
        textField.layer.borderColor = UIColor(named: NSDataAssetName(stringLiteral: "gray"))!.cgColor
    }
    
    // MARK: - keyboardDismiss
    @objc func keyboardDismiss(){
        view.endEditing(true)
    }
    
    // MARK: - backFunc
    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    // MARK: - keyboardGesture
    func keyboardGesture(){
        view.isUserInteractionEnabled = true
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(keyboardDismiss))
        view.addGestureRecognizer(gestureRecognizer)
    }
    
    // MARK: - setAppearance
    func setAppearance(){
        messageTextfield.delegate = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        userImageView.layer.cornerRadius = 25
        userImageView.layer.borderWidth = 1
        userImageView.layer.borderColor = UIColor.black.cgColor
        messageView.layer.cornerRadius = 25
        messageView.layer.borderWidth = 1
        messageView.layer.borderColor = UIColor.black.cgColor
        userImageView.sd_setImage(with: URL(string: otherUser.userImage!))
        userNameLabel.text = otherUser.userName
        userUsernameLabel.text = otherUser.userUsername
        
        keyboardGesture()
    }
    
    func createLabel(){
        // Önceki scrollView ve containerView'ı bulmak ve kaldırmak için etiketleri tanımla
        let scrollViewTag = 1001
        let containerViewTag = 1002
        
        // Önceki scrollView ve containerView'ı bul
        if let previousScrollView = view.viewWithTag(scrollViewTag),
           let previousContainerView = previousScrollView.viewWithTag(containerViewTag) {
            
            // Önceki scrollView'dan containerView'ı kaldır
            previousContainerView.removeFromSuperview()
            
            // Önceki scrollView'yı kaldır
            previousScrollView.removeFromSuperview()
        }
        
        let scrollView = UIScrollView(frame: CGRect(x:0 , y: 0, width: view.frame.width, height: view.frame.height))
        let containerView = UIView(frame: CGRect(x:0 , y: 0, width: view.frame.width, height: 1000))
        
        view.addSubview(scrollView)
        
        scrollView.tag = scrollViewTag
        containerView.tag = containerViewTag
        
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            scrollView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            scrollView.topAnchor.constraint(equalTo: userNameLabel.bottomAnchor, constant: 30),
            scrollView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            scrollView.bottomAnchor.constraint(equalTo: messageView.topAnchor)
        ])
        scrollView.contentSize = CGSize(width: view.frame.width, height: 1000)
        //        scrollView.backgroundColor = .brown
        
        containerView.translatesAutoresizingMaskIntoConstraints = false
        //        containerView.backgroundColor = .blue
        
        scrollView.addSubview(containerView)
        
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: scrollView.leadingAnchor),
            containerView.topAnchor.constraint(equalTo: scrollView.topAnchor),
            containerView.trailingAnchor.constraint(equalTo: scrollView.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor),
            containerView.widthAnchor.constraint(equalTo: scrollView.widthAnchor)
        ])
        
        
        for label in labels {
            label.removeFromSuperview()
        }
        labels.removeAll()
        
        for message in messages{
            let label = createMessageLabel(text: message.message)
            
            labels.append(label)
        }
        
        for (i,label) in labels.enumerated(){
            containerView.addSubview(label)
            if i == 0{
                label.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 15).isActive = true
            }
            else{
                label.topAnchor.constraint(equalTo: labels[i - 1].bottomAnchor, constant: 15).isActive = true
            }
            
            if messages[i].messageUserEmail == otherUser.userEmail{
                label.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 20).isActive = true
            }
            else{
                label.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -20).isActive = true
            }
            label.widthAnchor.constraint(lessThanOrEqualToConstant: 300).isActive = true
        }
    }
    
    // MARK: - createMessageLabel
    func createMessageLabel(text: String) -> UILabel {
        let label = PaddingLabel(withInsets: 6, 6, 16, 16)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.text = text
        label.font = UIFont.systemFont(ofSize: 16)
        label.numberOfLines = 0
        label.textColor = UIColor.black
        label.backgroundColor = UIColor(named: NSDataAssetName(stringLiteral: "lightGrayColor"))!
        label.layer.cornerRadius = 14
        label.clipsToBounds = true
        label.setContentHuggingPriority(.required, for: .vertical)
        label.layer.masksToBounds = true
        label.sizeToFit()
        label.lineBreakMode = .byCharWrapping
        
        return label
    }
}

// MARK: - class PaddingLabel
class PaddingLabel: UILabel {
    
    var topInset: CGFloat
    var bottomInset: CGFloat
    var leftInset: CGFloat
    var rightInset: CGFloat
    
    required init(withInsets top: CGFloat, _ bottom: CGFloat,_ left: CGFloat,_ right: CGFloat) {
        self.topInset = top
        self.bottomInset = bottom
        self.leftInset = left
        self.rightInset = right
        super.init(frame: CGRect.zero)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func drawText(in rect: CGRect) {
        let insets = UIEdgeInsets(top: topInset, left: leftInset, bottom: bottomInset, right: rightInset)
        super.drawText(in: rect.inset(by: insets))
    }
    
    override var intrinsicContentSize: CGSize {
        get {
            var contentSize = super.intrinsicContentSize
            contentSize.height += topInset + bottomInset
            contentSize.width += leftInset + rightInset
            return contentSize
        }
    }
    
    
}




