package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionHandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;



public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //监听对象不能交给spring管理，不能注入service，故
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData==null)
            throw new MyException(200001,"空文件");
        //逐行读取，一级分类、二级分类都不能重复添加
        EduSubject oneSubject=this.existOneSubject(eduSubjectService,subjectData.getOneSubjectName());
        //一级分类添加    
        if (oneSubject==null){
            oneSubject=new EduSubject();
            oneSubject.setParentId("0");
            oneSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(oneSubject);
        }
       //二级分类添加
        String id=oneSubject.getId();
        EduSubject twoSubject=this.existTwoSubject(eduSubjectService,subjectData.getTwoSubjectName(),id);

        if (twoSubject==null){
            twoSubject=new EduSubject();
            twoSubject.setParentId(id);
            twoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(twoSubject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    //一级分类判断
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject subject=eduSubjectService.getOne(wrapper);
        return subject;
    }
    //二级分类判断
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String id){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",id);
        EduSubject subject=eduSubjectService.getOne(wrapper);
        return subject;
    }

}
