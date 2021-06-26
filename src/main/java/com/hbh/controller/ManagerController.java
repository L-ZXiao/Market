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
import com.hbh.entity.Kcxx;
import com.hbh.entity.Manager;
import com.hbh.entity.Staff;
import com.hbh.service.imp.KcxxServiceImp;
import com.hbh.service.imp.ManagerServiceImp;
import com.hbh.service.imp.StaffServiceImp;
import com.hbh.tools.Constants;

/**
 * @Author Binvor
 * @Date 2019��4��16������1:56:55
 * @Des ����Ա���Ʋ�
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
	private Logger logger=Logger.getLogger(ManagerController.class);
	@Autowired
	ManagerServiceImp managerServiceImp;
	@Autowired
	StaffServiceImp staffServiceImp;
	
	@Autowired
	KcxxServiceImp kcxxServiceImp;
	@RequestMapping("/login")
	public String login() {
		logger.debug("LoginController welcome AppInfoSystem develpor==================");
		return "manager/managerlogin";
	}
	@RequestMapping(value="/dologin")
	public String dologin(@RequestParam String managerid,@RequestParam String pwd,
			HttpSession session,HttpServletRequest request) {
		Manager manager=managerServiceImp.getbyid(managerid, pwd);
		if(manager!=null) {
			session.setAttribute(Constants.Manager_SESSION, manager);
			return "redirect:/manager/flatform/main";
		}else {
			request.setAttribute("error", "�˺����벻ƥ��");
			return "manager/managerlogin";
		}
	}
	@RequestMapping(value="/flatform/main")
	public String main(HttpSession session,HttpServletRequest request) {
//		��֤�Ƿ���session��Ϣ����ֹ�Ƿ���¼��û�о���ת����¼ҳ��
		if(session.getAttribute(Constants.Manager_SESSION)==null)
		{
			return "redirect:/manager/login";
		}
		List<Kcxx> kcxx=kcxxServiceImp.kcxxWithProdata();
		List<Kcxx> kcxx2=kcxxServiceImp.kcxxWithPronum();
		if(!kcxx.isEmpty()||!kcxx2.isEmpty()) {
			request.setAttribute("msg", "����Ԥ����Ϣ�뼰ʱ����");
		}else {
			request.setAttribute("msg", "");
		}
		return "manager/main";
	}
	@RequestMapping(value="/logout")
	public String logout(HttpSession session) {
//		�˳����esison
		session.removeAttribute(Constants.Staff_SESSION);
		session.invalidate();
		return "redirect:/manager/login";
		
	}
	
	@RequestMapping("/getbyid")
	public String grxx(String managerid,HttpServletRequest request,Model model){
		 request.setAttribute("manager", managerServiceImp.getmanager(managerid));
	     model.addAttribute("manager",managerServiceImp.getmanager(managerid));  
	     return "manager/grxx"; 
		
	}
	@RequestMapping("/update")
    public String update(Manager manager,HttpServletRequest request,Model model){
    	if(managerServiceImp.update(manager)) {
    		manager=managerServiceImp.getmanager(manager.getManagerid());
    		model.addAttribute("manager", manager);
    		return "redirect:/manager/login"; 
    	}
    	return null;
    }
//  ��ת���޸ĸ�����Ϣҳ��
    
    @RequestMapping("/toupdate")  
	public String editmanager(Manager manager,HttpServletRequest request,Model model){
		model.addAttribute("manager", managerServiceImp.getmanager(manager.getManagerid()));
		return "manager/editmanager";
	}
    
//  ��ת������ҳ��
	
  @RequestMapping("/toadd")  
  public String toadd(){  
  	return "manager/addstaff";

  } 

//��ת���޸�Ա����Ϣҳ��
  
  @RequestMapping("/toupdatestaff")  
	public String editstaff(Staff staff,HttpServletRequest request,Model model){
		model.addAttribute("staff", staffServiceImp.getbyid(staff.getStaffid()));
		return "manager/editstaff";
	}   
  //�޸�Ա����Ϣ
	@RequestMapping("/updatestaff")
    public String updatestaff(Staff staff,HttpServletRequest request,Model model){
    	if(staffServiceImp.update(staff)) {
    		staff=staffServiceImp.getbyid(staff.getStaffid());
    		model.addAttribute("staff", staff);
    		return "redirect:getall"; 
    	}
    	return null;
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
		return "manager/getall_staff";
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
	return "manager/getstaffbyparams";
 
  }
	
}
