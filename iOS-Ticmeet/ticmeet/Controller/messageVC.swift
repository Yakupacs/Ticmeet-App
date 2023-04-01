//
//  messageVC.swift
//  ticmeet
//
//  Created by Yakup on 29.03.2023.
//

import UIKit
import Firebase

class messageVC: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var userUsernameLabel: UILabel!
    @IBOutlet weak var messageTextfield: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var messageView: UIView!
    
    var previousLabel: UILabel? = nil
    
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
        otherUser.userEmail = "muratcantaner@gmail.com"
        getMessages()
    }
    
    // MARK: - getMessages()
    func getMessages(){
        let firestore = Firestore.firestore()
        firestore.collection("Message").whereField("usersEmail", isEqualTo: [mainUser.userEmail, otherUser.userEmail]).addSnapshotListener { querySnapshot, error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                // START SNAPSHOT
                self.messages = []
                
                guard let querySnapshot = querySnapshot else { return }
                
                print("Document sayısı: \(querySnapshot.documents.count)")
                for document in querySnapshot.documents{
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
                //                print("Mesaj sayısı: \(self.messages.count)")
                //                for i in self.messages{
                //                    print(i.messageTime.timeIntervalSince1970)
                //                    let dateFormatter = DateFormatter()
                //                    dateFormatter.dateFormat = "dd/MM/yyyy HH:mm:ss"
                //                    let dateString = dateFormatter.string(from: i.messageTime)
                //                    print("\(dateString) Email: \(i.messageUserEmail)\nMessage: \(i.message)")
                //                }
                
                // FINISH SNAPSHOT
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
            self.messageView.topAnchor.constraint(equalTo: scrollView.bottomAnchor, constant: 0).isActive = true
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
        let containerView = UIView()

        if scrollView.subviews.count < 3{            
            scrollView.translatesAutoresizingMaskIntoConstraints = false
            scrollView.contentSize = CGSize(width: view.frame.width, height: 1000)
            scrollView.addSubview(containerView)
            containerView.translatesAutoresizingMaskIntoConstraints = false
            containerView.topAnchor.constraint(equalTo: scrollView.topAnchor).isActive = true
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor).isActive = true
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor).isActive = true
            containerView.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor).isActive = true
        }
        var sign = 0
        for message in messages{
            let label = createMessageLabel(text: message.message)
            containerView.addSubview(label)
            
            if let previousLabel = previousLabel {
                label.topAnchor.constraint(equalTo: previousLabel.bottomAnchor, constant: 15).isActive = true
            } else {
                label.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 15).isActive = true
            }
            
            if sign % 2 == 0{
                // SAĞ
                label.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -20).isActive = true
            }
            else{
                // SOL
                label.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 20).isActive = true
            }
            label.widthAnchor.constraint(lessThanOrEqualToConstant: 300).isActive = true
            sign += 1
            
            previousLabel = label
        }
        
        // Son mesajı alt tarafta tutun
        if let previousLabel = previousLabel {
            containerView.bottomAnchor.constraint(equalTo: previousLabel.bottomAnchor, constant: 10).isActive = true
        }
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




