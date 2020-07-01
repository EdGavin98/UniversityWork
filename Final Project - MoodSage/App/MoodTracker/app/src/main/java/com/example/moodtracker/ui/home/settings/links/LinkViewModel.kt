package com.example.moodtracker.ui.home.settings.links

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LinkViewModel @Inject constructor(private val accountRepo: AccountRepository) : ViewModel(),
    LinkButtonClickListener {

    private var _allLinks = MutableLiveData<List<LinkDto>>()
    val allLinks: LiveData<List<LinkDto>>
        get() = _allLinks

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    init {
        updateLinksList()
    }

    override fun onAcceptClicked(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(accountRepo.acceptLink(id))
        }
    }

    override fun onRemoveClicked(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(accountRepo.removeLink(id))
        }
    }

    private fun updateLinksList() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.getLinks().run {
                when (status) {
                    Status.SUCCESS -> _allLinks.postValue(data!!)
                    Status.FAILURE -> _message.postValue(message)
                }
            }
        }
    }

    private fun handleResponse(response: Resource<Unit>) {
        when (response.status) {
            Status.SUCCESS -> updateLinksList()
            Status.FAILURE -> _message.postValue(response.message)
        }
    }

}