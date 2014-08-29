//
//  XHNewsTableViewController.swift
//  XHNewsParsingSwift
//
//  Created by dw_iOS on 14-6-5.
//  Copyright (c) 2014年 广州华多网络科技有限公司 多玩事业部 iOS软件工程师 曾宪华. All rights reserved.
//写了一个sina news客户端，swift写的 有什么问题可以加swift群一起探讨,群号:328218600

import Foundation
import UIKit

class XHNewsTableViewController : UITableViewController {
    var dataSource = []
    
    var thumbQueue = NSOperationQueue()
    
    let hackerNewsApiUrl = "http://qingbin.sinaapp.com/api/lists?ntype=%E5%9B%BE%E7%89%87&pageNo=1&pagePer=10&list.htm"
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "下拉刷新")
        refreshControl.addTarget(self, action: "loadDataSource", forControlEvents: UIControlEvents.ValueChanged)
        self.refreshControl = refreshControl
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        loadDataSource();
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loadDataSource() {
        self.refreshControl.beginRefreshing()
        var loadURL = NSURL.URLWithString(hackerNewsApiUrl)
        var request = NSURLRequest(URL: loadURL)
        var loadDataSourceQueue = NSOperationQueue();
        
        NSURLConnection.sendAsynchronousRequest(request, queue: loadDataSourceQueue, completionHandler: { response, data, error in
            if error {
                println(error)
                dispatch_async(dispatch_get_main_queue(), {
                    self.refreshControl.endRefreshing()
                    })
            } else {
                let json = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: nil) as NSDictionary
                let newsDataSource = json["item"] as NSArray
                dispatch_async(dispatch_get_main_queue(), {
                    self.dataSource = newsDataSource
                    self.tableView.reloadData()
                    self.refreshControl.endRefreshing()
                    })
            }
            })
    }
    
    // #pragma mark - Segues
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showWebDetail" {
//            let indexPath = self.tableView.indexPathForSelectedRow()
//            let object = dataSource[indexPath.row] as NSDictionary
//            (segue.destinationViewController as XHWebViewDetailController).detailID = object["id"] as NSInteger
        }
    }

    
    // #pragma mark - Table View
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("XHNewsCell", forIndexPath: indexPath) as UITableViewCell
        
        let object = dataSource[indexPath.row] as NSDictionary
        
        cell.textLabel.text = object["title"] as String
        cell.detailTextLabel.text = object["id"] as String
        cell.imageView.image = UIImage(named :"cell_photo_default_small")
        cell.imageView.contentMode = UIViewContentMode.ScaleAspectFit
        
        let request = NSURLRequest(URL :NSURL.URLWithString(object["thumb"] as String))
        NSURLConnection.sendAsynchronousRequest(request, queue: thumbQueue, completionHandler: { response, data, error in
            if error {
                println(error)
                
            } else {
                let image = UIImage.init(data :data)
                dispatch_async(dispatch_get_main_queue(), {
                    cell.imageView.image = image
                    })
            }
            })
        
        
        return cell
    }
    
    override func tableView(tableView: UITableView!, heightForRowAtIndexPath indexPath: NSIndexPath!) -> CGFloat {
        return 80
    }
    
}
