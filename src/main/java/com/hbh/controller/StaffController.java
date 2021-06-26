package com.hbh.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbh.entity.Staff;
import com.hbh.service.imp.StaffServiceImp;
import com.hbh.tools.Constants;

/**
 * @Author Binvor
 * @Date 2019��3��15������2:19:15
 * @Des 
 */
@Controller
@RequestMapping("/staff")
public class StaffController {
	private Logger logger=Logger.getLogger(StaffController.class);
	@Autowired
	StaffServiceImp staffServiceImp;
	@RequestMapping("/login")
	public String login() {
		logger.debug("LoginController welcome AppInfoSystem develpor==================");
		return "stafflogin";
	}
	@RequestMapping(value="/dologin")
	public String dologin(@RequestParam String staffid,@RequestParam String pwd,
			HttpSession session,HttpServletRequest request) {
		Staff staff=staffServiceImp.getStaff(staffid, pwd);
		if(staff!=null) {
			session.setAttribute(Constants.Staff_SESSION, staff);
			return "redirect:/staff/flatform/main";
		}else {
			request.setAttribute("error", "�˺����벻ƥ��");
			return "stafflogin";
		}
	}
	@RequestMapping(value="/flatform/main")
	public String main(HttpSession session) {
//		��֤�Ƿ���session��Ϣ����ֹ�Ƿ���¼��û�о���ת����¼ҳ��
		if(session.getAttribute(Constants.Staff_SESSION)==null)
		{
			return "redirect:/staff/login";
		}
		return "main";
	}
	@RequestMapping(value="/logout")
	public String logout(HttpSession session) {
//		�˳����esison
		session.removeAttribute(Constants.Staff_SESSION);
		return "stafflogin";
		
	}
	@RequestMapping("/getbyid")
	public String grxx(String staffid,HttpServletRequest request,Model model){
		 request.setAttribute("staff", staffServiceImp.getbyid(staffid));
	     model.addAttribute("staff",staffServiceImp.getbyid(staffid));  
	     return "grxx"; 
		
	}
	@RequestMapping("/update")
    public String update(Staff staff,HttpServletRequest request,Model model){
    	if(staffServiceImp.update(staff)) {
    		staff=staffServiceImp.getbyid(staff.getStaffid());
    		model.addAttribute("custom", staff);
    		return "redirect:/staff/login"; 
    	}
    	return null;
    }
//  ��ת���޸�ҳ��
    
    @RequestMapping("/toupdate")  
	public String editstaff(Staff staff,HttpServletRequest request,Model model){
		model.addAttribute("staff", staffServiceImp.getbyid(staff.getStaffid()));
		return "editstaff";
	}
    
//  ��ת������ҳ��
	
  @RequestMapping("/toadd")  
  public String toadd(){  
  	return "addstaff";

  } 

    
   
//  ���ж����ݿ���û�У��о͸��£�û�о�����
    
    @RequestMapping("/insert")  
    public String insert(Staff staff,HttpServletRequest request,Model model){  
    	if(null==staffServiceImp.getbyid(staff.getStaffid())) {
    		staffServiceImp.insert(staff);    		
    	}else {
    		staffServiceImp.update(staff);
    	}
    	return "redirect:getall";

    } 
//    ɾ��
    
    @RequestMapping("/delete")
    public String delete(String staffid) {
    	staffServiceImp.delete(staffid);
    	return "redirect:getall";
    }
//  ��ѯ����
    
  @RequestMapping("/getall")
  public String getall_cus(ModelMap model,
			@RequestParam(defaultValue="1",required=true,value="pn") Integer pn
			) {
		PageHelper.startPage(pn, 4);
		List<Staff> staffs= staffServiceImp.getall();
		PageInfo<Staff> pageInfo=new PageInfo<Staff>(staffs);
		model.addAttribute("pageInfo", pageInfo);
		return "getall_staff";
  }
//����������ѯ
  @RequestMapping("getbyparams")
  public String getbyparams(@RequestParam(value="staffname",required=false)String staffname,@RequestParam(value="staffid",required=false)String staffid,
	@RequestParam(defaultValue="1",required=true,value="pn") Integer pn,HttpServletRequest request,Model model
		) {
	PageHelper.startPage(pn, 100);
	List<Staff> staffs= staffServiceImp.getbyparams(staffid, staffname);
	PageInfo<Staff> pageInfo=new PageInfo<Staff>(staffs);
	model.addAttribute("pageInfo", pageInfo);
	return "getstaffbyparams";
 
  }
}

