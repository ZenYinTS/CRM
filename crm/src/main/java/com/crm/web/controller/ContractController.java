package com.crm.web.controller;

import com.crm.domain.Contract;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.ContractQueryObject;
import com.crm.service.IContractService;
import com.crm.util.AjaxResult;
import com.crm.util.UploadUtils;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Controller
public class ContractController {

    @Autowired
    private IContractService contractService;

    @RequestMapping("/contract")
    public String list() {
        return "contract";
    }

    @ResponseBody
    @RequestMapping("/contract_list")
    public PageResult queryForPage(ContractQueryObject queryObject) {
        return contractService.queryForPage(queryObject);
    }


    @ResponseBody
    @RequestMapping("/contract_save")
    public AjaxResult save(Contract contract, @RequestParam(value = "pic", required = false) MultipartFile pic, HttpServletRequest request) {
        String filePath = UploadUtils.mulipartFileUpload(pic, request);
        AjaxResult result = null;
        contract.setStatus((short) 0);
        contract.setFile(filePath);
        contract.setSn(UUID.randomUUID().toString());
        try {
            contractService.insert(contract);
            result = new AjaxResult(true, "添加成功！");
        } catch (Exception e) {
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/contract_update")
    public AjaxResult update(Contract contract, @RequestParam(value = "pic", required = false) MultipartFile pic, HttpSession session, HttpServletRequest request) {
        String filePath = UploadUtils.mulipartFileUpload(pic, request);
        Employee emp = (Employee) session.getAttribute(UserContext.USERINSESSION);
        AjaxResult result = null;
        //不在对话框中的字段赋值
        contract.setFile(filePath);
        contract.setModifyuser(emp);
        contract.setModifytime(new Date());
        try {
            contractService.updateByPrimaryKey(contract);
            result = new AjaxResult(true, "更新成功！");
        } catch (Exception e) {
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/contract_remove")
    public AjaxResult remove(Long id) {
        AjaxResult result = null;
        try {
            contractService.deleteByPrimaryKey(id);
            result = new AjaxResult(true, "删除成功！");
        } catch (Exception e) {
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/contract_check")
    public AjaxResult check(Long id, String option) {
        AjaxResult result = null;
        try {
            Contract contract = new Contract();
            contract.setId(id);
            if ("permit".equals(option))
                contract.setStatus((short) 1);
            if ("reject".equals(option))
                contract.setStatus((short) 2);
            contractService.updateByPrimaryKey(contract);
            result = new AjaxResult(true, "审核完成！");
        } catch (Exception e) {
            System.out.println(e);
            result = new AjaxResult("审核失败，请联系管理员！");
        }
        return result;
    }

    @RequestMapping("/contract_download")
    public ResponseEntity<byte[]> download(String path) throws Exception {
        return UploadUtils.download(path);
    }
}
