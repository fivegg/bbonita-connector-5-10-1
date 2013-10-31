/**
 * SugarsoapPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bonitasoft.connectors.sugar.common.soap.v2;

public interface SugarsoapPortType extends java.rmi.Remote {
    public org.bonitasoft.connectors.sugar.common.soap.v2.Entry_value login(org.bonitasoft.connectors.sugar.common.soap.v2.User_auth user_auth, java.lang.String application_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list) throws java.rmi.RemoteException;
    public void logout(java.lang.String session) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_entry(java.lang.String session, java.lang.String module_name, java.lang.String id, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_entries(java.lang.String session, java.lang.String module_name, java.lang.String[] ids, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_list_result_version2 get_entry_list(java.lang.String session, java.lang.String module_name, java.lang.String query, java.lang.String order_by, int offset, java.lang.String[] select_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] link_name_to_fields_array, int max_results, int deleted) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_relationship_list_result set_relationship(java.lang.String session, java.lang.String module_name, java.lang.String module_id, java.lang.String link_field_name, java.lang.String[] related_ids, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list, int delete) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_relationship_list_result set_relationships(java.lang.String session, java.lang.String[] module_names, java.lang.String[] module_ids, java.lang.String[] link_field_names, java.lang.String[][] related_ids, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[][] name_value_lists, int[] delete_array) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_result_version2 get_relationships(java.lang.String session, java.lang.String module_name, java.lang.String module_id, java.lang.String link_field_name, java.lang.String related_module_query, java.lang.String[] related_fields, org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array[] related_module_link_name_to_fields_array, int deleted) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_entry(java.lang.String session, java.lang.String module_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[] name_value_list) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entries_result set_entries(java.lang.String session, java.lang.String module_name, org.bonitasoft.connectors.sugar.common.soap.v2.Name_value[][] name_value_lists) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_server_info_result get_server_info() throws java.rmi.RemoteException;
    public java.lang.String get_user_id(java.lang.String session) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_module_fields get_module_fields(java.lang.String session, java.lang.String module_name, java.lang.String[] fields) throws java.rmi.RemoteException;
    public int seamless_login(java.lang.String session) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_note_attachment(java.lang.String session, org.bonitasoft.connectors.sugar.common.soap.v2.New_note_attachment note) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_return_note_attachment get_note_attachment(java.lang.String session, java.lang.String id) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result set_document_revision(java.lang.String session, org.bonitasoft.connectors.sugar.common.soap.v2.Document_revision note) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.New_return_document_revision get_document_revision(java.lang.String session, java.lang.String i) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Return_search_result search_by_module(java.lang.String session, java.lang.String search_string, java.lang.String[] modules, int offset, int max_results) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Module_list get_available_modules(java.lang.String session) throws java.rmi.RemoteException;
    public java.lang.String get_user_team_id(java.lang.String session) throws java.rmi.RemoteException;
    public void set_campaign_merge(java.lang.String session, java.lang.String[] targets, java.lang.String campaign_id) throws java.rmi.RemoteException;
    public org.bonitasoft.connectors.sugar.common.soap.v2.Get_entries_count_result get_entries_count(java.lang.String session, java.lang.String module_name, java.lang.String query, int deleted) throws java.rmi.RemoteException;
}
