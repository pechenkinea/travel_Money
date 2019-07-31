package com.pechenkin.travelmoney.page;

import com.pechenkin.travelmoney.speech.recognition.CostCreator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pechenkin on 20.04.2018.
 */

public class PageParam {

    private CostCreator costCreator;

    private PageParam(){}

    private long id;
    private String photoUrl;
    private String name;
    private String description;
    private double sum = 0f;
    private Date selectDate;
    private Set<Long> selectedIds;

    public long getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    private String getDescription() {
        return description;
    }

    public double getSum() {
        return sum;
    }

    public Date getSelectDate() {
        return selectDate;
    }

    public Set<Long> getSelectedIds() {
        return selectedIds;
    }

    public CostCreator getCostCreator() {
        return costCreator;
    }


    static public class BuildingPageParam
    {
        private long id = -1;
        private String photoUrl = "";
        private String name = "";
        private String description = "";
        private double sum = 0f;
        private Date selectDate = new Date();
        private Set<Long> selectedIds = new HashSet<>();
        private CostCreator costCreator = null;

        public BuildingPageParam(){

        }

        public BuildingPageParam(PageParam param)
        {
            id = param.getId();
            photoUrl = param.photoUrl;
            name = param.getName();
            description = param.getDescription();
            sum = param.getSum();
            selectDate = param.getSelectDate();
            selectedIds = param.getSelectedIds();
            costCreator = param.getCostCreator();
        }

        public PageParam getParam()
        {
            PageParam pageParam = new PageParam();
            pageParam.id = this.id;
            pageParam.photoUrl = this.photoUrl;
            pageParam.name = this.name;
            pageParam.description = this.description;
            pageParam.sum = this.sum;
            pageParam.selectDate = this.selectDate;
            pageParam.selectedIds = this.selectedIds;
            pageParam.costCreator = this.costCreator;
            return pageParam;
        }

        public BuildingPageParam setId(long id)
        {
            this.id = id;
            return this;
        }
        public BuildingPageParam setPhotoUrl(String photoUrl)
        {
            this.photoUrl = photoUrl;
            return this;
        }

        public BuildingPageParam setName(String name)
        {
            this.name = name;
            return this;
        }

        public BuildingPageParam setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public BuildingPageParam setSum(double sum)
        {
            this.sum = sum;
            return this;
        }

        public BuildingPageParam setSelectDate(Date date)
        {
            this.selectDate = date;
            return this;
        }
        public BuildingPageParam setSelectedIds(Set<Long> selectedIds)
        {
            this.selectedIds = selectedIds;
            return this;
        }

        public BuildingPageParam setCostCreator(CostCreator costCreator)
        {
            this.costCreator = costCreator;
            return this;
        }

    }
}
