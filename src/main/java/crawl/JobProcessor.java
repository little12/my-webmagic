package crawl;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class JobProcessor implements PageProcessor {
    //解析页面
    public void process(Page page) {
        //解析返回的数据page，并且把解析的结果放到ResultItems中
        // getHtml获取目标页面 div标签 class为mt 下面的h2标签的所有元素
        page.putField("div", page.getHtml().css("ul.fr a").all());

        //Xpath
        page.putField("div2",page.getHtml().xpath("//div[@id=shortcut]/div/ul/li/a"));
    }

    private Site site=Site.me();
    public Site getSite() {
        return site;
    }

    //主函数 执行爬虫
    public static void main(String[] args){
        Spider.create(new JobProcessor())
                .addUrl("https://www.jd.com/moreSubject.aspx") //设置爬虫的页面
                .run();
    }
}
