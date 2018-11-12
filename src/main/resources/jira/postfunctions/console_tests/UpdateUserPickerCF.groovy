package jira.postfunctions.console_tests

import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.user.ApplicationUser

def issueManager = ComponentAccessor.issueManager
def issue = issueManager.getIssueObject("TESSR-1") as MutableIssue

def userPickerCF = ComponentAccessor.customFieldManager.getCustomFieldObject(10024l)
def currentValue = userPickerCF.getValue(issue) as ApplicationUser

if (currentValue == null) {
    def issueService = ComponentAccessor.issueService
    def issueInputParameters = issueService.newIssueInputParameters()
    def currentUser = ComponentAccessor.jiraAuthenticationContext.loggedInUser
    issueInputParameters.addCustomFieldValue(userPickerCF.idAsLong, currentUser.key).setSkipScreenCheck(true)
    IssueService.UpdateValidationResult validationResult = issueService.validateUpdate(currentUser, issue.id, issueInputParameters)
    if (validationResult.valid) issueService.update(currentUser, validationResult)
}



