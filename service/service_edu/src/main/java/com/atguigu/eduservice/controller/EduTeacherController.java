package com.atguigu.eduservice.controller;



import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionHandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author w
 * @since 2022-04-09
 *
 */
@CrossOrigin
//组合注解
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @GetMapping("findAll")
    public R findAll(){
        return R.ok().data("item",eduTeacherService.list(null));
    }

    /*逻辑删除讲师*/
    @DeleteMapping("{id}")
    public R delete(@PathVariable String id){
        boolean flag=eduTeacherService.removeById(id);

        if (flag){
           return R.ok();

        }

        else {
           return R.error();
        }

    }
    /*分页*/
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> teacherPage=new Page<>(current,limit);

        eduTeacherService.page(teacherPage,null);
        long total=teacherPage.getTotal();
        List<EduTeacher> list=teacherPage.getRecords();
        Map map=new HashMap();
        map.put("rows",list);
        map.put("total",total);
        return R.ok().data(map);
    }

    /*条件查询*/
    @PostMapping("pageTeacherCondition/{current}/{limit}")                                    /*表示值可以没有*/
    public R queryByCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> page=new Page<>(current,limit);
        //构件条件
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        /*判断条件是否为空，mybatis阶段需要在xml文件中写动态sql拼接条件*/
        String name=teacherQuery.getName();
        Integer level=teacherQuery.getLevel();
        String begin=teacherQuery.getBegin();
        String end=teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        wrapper.orderByDesc("gmt_create");
        eduTeacherService.page(page,wrapper);

        Long total=page.getTotal();
        List<EduTeacher> list=page.getRecords();
        Map map=new HashMap();
        map.put("total",total);
        map.put("rows",list);
        return R.ok().data(map);

    }

    /*添加讲师*/
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save=eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }
        return R.error();
    }

    /*根据id进行查询*/
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {

        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    /*修改讲师*/
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update=eduTeacherService.updateById(eduTeacher);
        if (update)
            return R.ok();
        return R.error();
    }
}

