//
//  tabbarController.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import UIKit

class tabbarController: UITabBarController {
    
    var tabbar = UITabBar()
    //    var tabbarBackgroundColor: UIColor = UIColor(named: NSDataAssetName(stringLiteral: "tabbarBackgroundColor"))!
    var tabbarBackgroundColor: UIColor = UIColor.black
    @IBOutlet weak var myTabBar: UITabBar!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidLayoutSubviews() {
        self.changeRadiusOfTabbar()
        self.selectedItemBackground(color: tabbarBackgroundColor)
        tabbar = tabBar
        for item in tabbar.items!{
            item.setCustomSize(size: CGSizeMake(30, 30))
            if item == tabbar.selectedItem{
                item.setCustomSize(size: CGSizeMake(30, 30))
            }
        }
    }
    
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        self.changeRadiusOfTabbar()
        self.selectedItemBackground(color: tabbarBackgroundColor)
        tabbar = tabBar
        for item in tabbar.items!{
            item.setCustomSize(size: CGSizeMake(30, 30))
            if item == tabbar.selectedItem{
                item.setCustomSize(size: CGSizeMake(30, 30))
            }
        }
        
        self.simpleAnimationWhenSelectItem(item: item)
    }
    
    func simpleAnimationWhenSelectItem(item : UITabBarItem)
    {
        guard let barItemView = item.value(forKey: "view") as? UIView else { return }
        
        let timeInterval: TimeInterval = 0.45
        let propertyAnimator = UIViewPropertyAnimator(duration: timeInterval, dampingRatio: 0.5){
            barItemView.transform = CGAffineTransform.identity.scaledBy(x: 1.2, y: 1.2)
        }
        propertyAnimator.addAnimations({barItemView.transform = .identity},
                                       delayFactor: CGFloat(timeInterval))
        propertyAnimator.startAnimation()
    }
    
    func changeRadiusOfTabbar()
    {
        self.tabBar.layer.masksToBounds = true
        self.tabBar.isTranslucent = true
        self.tabBar.layer.cornerRadius = 25
        self.tabBar.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
    }
    
    func selectedItemBackground(color: UIColor){
        tabBar.selectionIndicatorImage = UIImage.imageWithColor(color: color, size: CGSize(width: 150, height: 150)).resizableImage(withCapInsets: UIEdgeInsets.zero)
    }
    
    func changeHeightOfTabbar()
    {
        if UIDevice().userInterfaceIdiom == .phone {
            var tabFrame    = tabBar.frame
            tabFrame.size.height    = 100
            tabFrame.origin.y   = view.frame.size.height - 100
            tabBar.frame =  tabFrame
        }
    }
}

extension UIImage {
    class func imageWithColor(color: UIColor, size: CGSize) -> UIImage {
        let rect: CGRect = CGRect(x: 0, y: 0, width: size.width, height: size.height)
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        color.setFill()
        UIRectFill(rect)
        let image: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return image
    }
    
    func resized(to size: CGSize) -> UIImage? {
        let renderer = UIGraphicsImageRenderer(size: size)
        return renderer.image { _ in
            self.draw(in: CGRect(origin: .zero, size: size))
        }
    }
}
extension UITabBarItem {
    func setCustomSize(size: CGSize) {
        // Tab bar item'ın resmi nil değilse, yeni boyutu ayarlayın.
        if let image = self.image {
            self.image = image.resized(to: size)
        }
        // Tab bar item'ın resmi nil değilse, yeni boyutu ayarlayın.
        if let selectedImage = self.selectedImage {
            self.selectedImage = selectedImage.resized(to: size)
        }
    }
}
