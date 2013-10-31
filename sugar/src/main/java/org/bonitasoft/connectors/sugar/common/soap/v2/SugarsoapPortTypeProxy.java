package org.bonitasoft.connectors.sugar.common.soap.v2;

public class SugarsoapPortTypeProxy implements org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapPortType {
  private String _endpoint = null;
  private org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapPortType sugarsoapPortType = null;
  
  public SugarsoapPortTypeProxy() {
    _initSugarsoapPortTypeProxy();
  }
  
  public SugarsoapPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initSugarsoapPortTypeProxy();
  }
  
  private void _initSugarsoapPortTypeProxy() {
    try {
      sugarsoapPortType = (new org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapLocator()).getsugarsoapPort();
      if (sugarsoapPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sugarsoapPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sugarsoapPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sugarsoapPortType != null)
      ((javax.xml.rpc.Stub)sugarsoapPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapPortType getSugarsoapPortType() {
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType;
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Entry_value login(org.bonitasoft.connectors.sugar.common.soap.v2.User_auth user_auth, java.lang.String application_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.login(user_auth, application_name, name_value_list);
  }
  
  public void logout(java.lang.String session) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    sugarsoapPortType.logout(session);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_entry(java.lang.String session, java.lang.String module_name, java.lang.String id, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_entry(session, module_name, id, select_fields, link_name_to_fields_array);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_entries(java.lang.String session, java.lang.String module_name, java.lang.String[] ids, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_entries(session, module_name, ids, select_fields, link_name_to_fields_array);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_list_result_version2 get_entry_list(java.lang.String session, java.lang.String module_name, java.lang.String query, java.lang.String order_by, int offset, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array, int max_results, int deleted) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_entry_list(session, module_name, query, order_by, offset, select_fields, link_name_to_fields_array, max_results, deleted);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_relationship_list_result set_relationship(java.lang.String session, java.lang.String module_name, java.lang.String module_id, java.lang.String link_field_name, java.lang.String[] related_ids, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list, int delete) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_relationship(session, module_name, module_id, link_field_name, related_ids, name_value_list, delete);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_relationship_list_result set_relationships(java.lang.String session, java.lang.String[] module_names, java.lang.String[] module_ids, java.lang.String[] link_field_names, java.lang.String[][] related_ids, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[][] name_value_lists, int[] delete_array) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_relationships(session, module_names, module_ids, link_field_names, related_ids, name_value_lists, delete_array);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_relationships(java.lang.String session, java.lang.String module_name, java.lang.String module_id, java.lang.String link_field_name, java.lang.String related_module_query, java.lang.String[] related_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] related_module_link_name_to_fields_array, int deleted) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_relationships(session, module_name, module_id, link_field_name, related_module_query, related_fields, related_module_link_name_to_fields_array, deleted);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_entry(java.lang.String session, java.lang.String module_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_entry(session, module_name, name_value_list);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entries_result set_entries(java.lang.String session, java.lang.String module_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[][] name_value_lists) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_entries(session, module_name, name_value_lists);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_server_info_result get_server_info() throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_server_info();
  }
  
  public java.lang.String get_user_id(java.lang.String session) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_user_id(session);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_module_fields get_module_fields(java.lang.String session, java.lang.String module_name, java.lang.String[] fields) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_module_fields(session, module_name, fields);
  }
  
  public int seamless_login(java.lang.String session) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.seamless_login(session);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_note_attachment(java.lang.String session, org.bonitasoft.connectors.sugar.common.soap.v2.New_note_attachment note) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_note_attachment(session, note);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_return_note_attachment get_note_attachment(java.lang.String session, java.lang.String id) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_note_attachment(session, id);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_document_revision(java.lang.String session, org.bonitasoft.connectors.sugar.common.soap.v2.Document_revision note) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.set_document_revision(session, note);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.New_return_document_revision get_document_revision(java.lang.String session, java.lang.String i) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_document_revision(session, i);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Return_search_result search_by_module(java.lang.String session, java.lang.String search_string, java.lang.String[] modules, int offset, int max_results) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.search_by_module(session, search_string, modules, offset, max_results);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Module_list get_available_modules(java.lang.String session) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_available_modules(session);
  }
  
  public java.lang.String get_user_team_id(java.lang.String session) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_user_team_id(session);
  }
  
  public void set_campaign_merge(java.lang.String session, java.lang.String[] targets, java.lang.String campaign_id) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    sugarsoapPortType.set_campaign_merge(session, targets, campaign_id);
  }
  
  public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entries_count_result get_entries_count(java.lang.String session, java.lang.String module_name, java.lang.String query, int deleted) throws java.rmi.RemoteException{
    if (sugarsoapPortType == null)
      _initSugarsoapPortTypeProxy();
    return sugarsoapPortType.get_entries_count(session, module_name, query, deleted);
  }
  
  
}