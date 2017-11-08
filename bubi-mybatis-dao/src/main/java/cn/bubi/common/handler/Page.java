package cn.bubi.common.handler;

import java.util.HashMap;
import java.util.List;

public class Page<T> extends HashMap<String, Object>{

    private static final long serialVersionUID = -6320939471752470785L;

    private Integer pageBegin = 1;

    private Integer fetchBegin = 0;

    private Integer fetchNum = 10;

    private Integer totalNums;

    private Integer totalPages;

    private List<T> resultList;

    public Page(Integer pageBegin, Integer fetchNum){
        if (pageBegin == null || pageBegin < 1) {
            pageBegin = 1;
        }
        if (fetchNum == null || fetchNum < 1) {
            fetchNum = 10;
        }

        this.fetchNum = fetchNum;
        this.pageBegin = pageBegin;
        this.fetchBegin = (pageBegin - 1) * fetchNum;
        this.put("fetchBegin", fetchBegin);
        this.put("fetchNum", fetchNum);
    }

    public Page(Integer pageBegin, Integer fetchNum, Integer totalNums, List<T> resultList){
        this.pageBegin = pageBegin;
        this.fetchNum = fetchNum;
        this.totalNums = totalNums;
        if (totalNums == null || totalNums <= 0) {
            totalPages = 0;
            this.resultList = null;
        } else {
            totalPages = (totalNums - 1) / fetchNum + 1;
            this.resultList = resultList;
        }

    }

    public Integer getPageBegin(){
        return pageBegin;
    }

    public void setPageBegin(Integer pageBegin){
        this.pageBegin = pageBegin;
    }

    public Integer getFetchNum(){
        return fetchNum;
    }

    public void setFetchNum(Integer fetchNum){
        this.fetchNum = fetchNum;
    }

    public List<T> getResultList(){
        return resultList;
    }

    public void setResultList(List<T> resultList){
        this.resultList = resultList;
    }

    public boolean haveNextPage(){
        return totalPages > pageBegin;
    }

    public boolean havePrePage(){
        return pageBegin > 1;
    }

    public Integer getTotalNums(){
        return totalNums;
    }

    public void setTotalNums(Integer totalNums){
        this.totalNums = totalNums;
    }

    public Integer getTotalPages(){
        return totalPages;
    }

    public void setTotalPages(Integer totalPages){
        this.totalPages = totalPages;
    }

    public Integer getFetchBegin(){
        return fetchBegin;
    }

    public void setFetchBegin(Integer fetchBegin){
        this.fetchBegin = fetchBegin;
    }

    @Override
    public String toString(){
        return "Page{" +
                "pageBegin=" + pageBegin +
                ", fetchBegin=" + fetchBegin +
                ", fetchNum=" + fetchNum +
                ", totalNums=" + totalNums +
                ", totalPages=" + totalPages +
                ", resultList=" + resultList +
                "} ";
    }
}
