package com.pechenkin.travelmoney.page;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
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


    private int pageId;
    private Member member;
    private Member toMember;
    private String photoUrl;
    private String name;
    private String description;
    private int sum = 0;
    private Date selectDate;
    private Set<Member> selectedMembers;
    private Trip trip;

    private Class<? extends Page> backPage;


    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    private String getDescription() {
        return description;
    }

    public int getSum() {
        return sum;
    }

    public Date getSelectDate() {
        return selectDate;
    }

    public Set<Member> getSelectedMembers() {
        return selectedMembers;
    }

    public CostCreator getCostCreator() {
        return costCreator;
    }

    public Class<? extends Page> getBackPage() {
        return backPage;
    }

    public Member getToMember() {
        return toMember;
    }

    public Member getMember() {
        return member;
    }

    public Trip getTrip() {
        return trip;
    }

    public int getPageId() {
        return pageId;
    }

    static public class BuildingPageParam
    {
        private int pageId;
        private Member member;
        private Member toMember;
        private Trip trip;
        private String photoUrl = "";
        private String name = "";
        private String description = "";
        private int sum = 0;
        private Date selectDate = new Date();
        private Set<Member> selectedMembers = new HashSet<>();
        private CostCreator costCreator = null;
        private Class<? extends Page> backPage = null;

        public BuildingPageParam(){

        }

        public BuildingPageParam(PageParam param)
        {
            photoUrl = param.photoUrl;
            name = param.getName();
            description = param.getDescription();
            sum = param.getSum();
            selectDate = param.getSelectDate();
            selectedMembers = param.getSelectedMembers();
            costCreator = param.getCostCreator();
            backPage = param.getBackPage();
            member = param.getMember();
            toMember = param.getToMember();
            trip = param.getTrip();
            pageId = param.getPageId();
        }


        public PageParam getParam()
        {
            PageParam pageParam = new PageParam();
            pageParam.photoUrl = this.photoUrl;
            pageParam.name = this.name;
            pageParam.description = this.description;
            pageParam.sum = this.sum;
            pageParam.selectDate = this.selectDate;
            pageParam.selectedMembers = this.selectedMembers;
            pageParam.costCreator = this.costCreator;
            pageParam.backPage = this.backPage;
            pageParam.toMember = this.toMember;
            pageParam.member = this.member;
            pageParam.trip = this.trip;
            pageParam.pageId = this.pageId;

            return pageParam;
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

        public BuildingPageParam setSum(int sum)
        {
            this.sum = sum;
            return this;
        }

        public BuildingPageParam setSelectDate(Date date)
        {
            this.selectDate = date;
            return this;
        }
        public BuildingPageParam setSelectedMembers(Set<Member> selectedMembers)
        {
            this.selectedMembers = selectedMembers;
            return this;
        }

        public BuildingPageParam setCostCreator(CostCreator costCreator)
        {
            this.costCreator = costCreator;
            return this;
        }

        public BuildingPageParam setBackPage(Class<? extends Page> backPage) {
            this.backPage = backPage;
            return this;
        }

        public BuildingPageParam setMember(Member member) {
            this.member = member;
            return this;
        }
        public BuildingPageParam setToMember(Member toMember) {
            this.toMember = toMember;
            return this;
        }

        public BuildingPageParam setTrip(Trip trip) {
            this.trip = trip;
            return this;
        }

        public BuildingPageParam setPageId(int pageId) {
            this.pageId = pageId;
            return this;
        }
    }
}
