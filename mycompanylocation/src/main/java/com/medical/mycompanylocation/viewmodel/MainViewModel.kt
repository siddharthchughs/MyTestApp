package com.medical.mycompanylocation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.medical.mycompanylocation.repository.RepositoryCompanyMoshi


class MainViewModel(application: Application): AndroidViewModel(application){

    val companyRepo = RepositoryCompanyMoshi(application)
    val data  = companyRepo.companyInfomutable

}