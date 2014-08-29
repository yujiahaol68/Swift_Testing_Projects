//
//  ThreeView.swift
//  Swift Demo
//
//  Created by Liang Haiyan on 6/6/14.
//  Copyright (c) 2014 Synjones. All rights reserved.
//

import UIKit

class ThreeView: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor.yellowColor()
        
        var button: UIButton = UIButton(frame: CGRectMake(10, 100, self.view.frame.size.width-20, 30))
        button.backgroundColor = UIColor.greenColor()
        button.setTitle("Button", forState: UIControlState.Normal)
        button.titleLabel.font = UIFont.systemFontOfSize(12)
        button.addTarget(self, action: "tappedButton:", forControlEvents: UIControlEvents.TouchUpInside)
        self.view.addSubview(button)
    }

    func tappedButton(sender: UIButton) {
        var v: UIViewController = UIViewController()
        v.title = "View Controller"
        self.navigationController.pushViewController(v, animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /*
    // #pragma mark - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue?, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

}
