package crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JobProcessor implements PageProcessor {
    private int pageTotal = 1;
    private static String url = "http://bcc.blcu.edu.cn/zh/search/35/%E6%89%94%E5%9C%A8";
    private String word = "扔在";
    //解析页面
    public void process(Page page) {
        //selenium自动控制
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();//窗口最大化
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
        driver.get(url);

        //添加句柄
        String currentWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> it = handles.iterator();
        while (it.hasNext()) {
            String handle = it.next();
            if (currentWindow.equals(handle))
                continue;
            driver.switchTo().window(handle);
        }

        //获取到元素列表
        List<WebElement> list = driver.findElements(By.xpath("//td[contains(text(),'CJba')]"));
        List<String> listStr=new ArrayList<String>();
        for(WebElement element:list){
            listStr.add(element.getText());
            //System.out.println(element.getText());
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //从第二页开始迭代
        for(int i=1;i<pageTotal;i++){
            //选择下一页元素进行点击
            driver.findElement(By.cssSelector("div.btn-container ul.pager button:nth-of-type(3)")).click();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获取到元素列表
            List<WebElement> list1 = driver.findElements(By.xpath("//td[contains(text(),'CJba')]"));
            // List<String> listStr1=new ArrayList<String>();
            for(WebElement element:list1){
                listStr.add(element.getText());
                //System.out.println(element.getText());
            }
        }
        page.putField(word,listStr);
       // page.putField("div3",page.getHtml().css("div.container-fluid table tbody td.col-md-5").regex(".*CJba.*").all());
    }

    private Site site = Site.me()
            .setSleepTime(1000)
            .setCycleRetryTimes(3);
    public Site getSite() {
        return site;
    }


    //主函数 执行爬虫
    public static void main(String[] args){
        System.setProperty("selenuim_config", "C:\\Users\\LISHUANG\\Downloads\\webmagic-crawl\\src\\main\\resources\\selenmium.properties");
        Spider.create(new JobProcessor())
                .addUrl(url) //设置爬虫的页面
                .addPipeline(new FilePipeline("C:\\Users\\LISHUANG\\Desktop\\result"))//输出到文件
                .thread(5)//多线程
                .setDownloader(new SeleniumDownloader("E:\\chromedriver.exe").setSleepTime(3000))
                .run();
    }
}
