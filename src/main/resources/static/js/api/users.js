const API_URL = '/api/user';

$(document).ready(function () {
    refresh_users_table();
    bind_user_form_buttons();
});

// clean user form for create tab
$('#create-tab').on('click', function (e) {
    e.preventDefault();
    // $('#create').find('#user-form').trigger('reset');
    $(this).tab('show')
});

function refresh_users_table() {
    $.get(API_URL + '/all', users => {
        $("#user-table-body").find('.user').remove();
        if ($.isArray(users)) {
            users.forEach(user => {
                let user_row = $("#user-row").clone();
                $(user_row).attr('id', user.id);
                $(user_row).find('th,td').each((index, data) => {
                    let key = $(data).attr('class');
                    let value = $.isArray(user[key]) ? user[key].map(elem => elem.name.toLowerCase()) : user[key];
                    $(data).text(value);
                });
                $("#user-table-body").append(user_row);
            });
            bind_user_rows_click();
        }
    });
}

// bind user form buttons onclick
function bind_user_form_buttons() {
    // create button
    const user_create_form = $('#create').find('#user-form');
    const create_button = $(user_create_form).find('#create-button');
    if ($(create_button)) {
        $(create_button).on('click', event => {
            event.preventDefault();
            let user = user_form_read(user_create_form);
            // send request
            user_form_send(user_create_form, API_URL, 'POST', user,
                result => {
                    console.log(result);
                    alert_show('success', 'User ' + user.username + ' successful created.');
                    refresh_users_table();
                    $('#users-tab').tab('show');
                },
                (request, message, error) => {
                    console.log(request, message, error);
                    alert_show('danger', 'User ' + user.username + ' create failed.');
                    refresh_users_table();
                    $('#users-tab').tab('show');
                });
        });
    }
    //
    const user_edit_form = $('#users').find('#user-form');
    // update button
    const update_button = $('#update-button');
    if ($(update_button)) {
        $(update_button).on('click', event => {
            event.preventDefault();
            let user = user_form_read(user_edit_form);
            // send request
            user_form_send(user_edit_form, API_URL, 'PUT', user,
                result => {
                    console.log(result);
                    alert_show('success', 'User ' + user.username + ' successful updated.');
                    $('#user-modal').modal('hide');
                    refresh_users_table();
                },
                (request, message, error) => {
                    console.log(request, message, error);
                    alert_show('danger', 'User ' + user.username + ' update failed.');
                    $('#user-modal').modal('hide');
                    refresh_users_table();
                });
        });
    }
    // delete button
    const delete_button = $('#delete-button');
    if ($(delete_button)) {
        $(delete_button).on('click', event => {
            event.preventDefault();
            let username = $(user_edit_form).find('#username').attr('value');
            let href = API_URL + '/' + $(user_edit_form).find('#id').attr('value');
            // send request
            user_form_send(user_edit_form, href, 'DELETE', null,
                result => {
                    console.log(result);
                    alert_show('success', 'User ' + username + ' successful deleted.');
                    $('#user-modal').modal('hide');
                    refresh_users_table();
                },
                (request, message, error) => {
                    console.log(request, message, error);
                    alert_show('danger', 'User ' + username + ' delete failed.');
                    $('#user-modal').modal('hide');
                    refresh_users_table();
                });
        });
    }
}

// bind user rows click
function bind_user_rows_click() {
    const user_form = $('#user-form');
    const csrf = $(user_form).find('input[name="_csrf"]').val();
    $('.user').on('click', function (event) {
        event.preventDefault();
        let href = API_URL + '/' + $(this).attr('id');
        $.ajax({
            url: href,
            type: 'GET',
            headers: {'X-CSRF-TOKEN': csrf},
            success: user => {
                // reset form inputs val()
                $(user_form).trigger('reset');
                // fill inputs
                user_form_write(user_form, user);
                $('#user-modal-title').text('Edit user ' + user.username);
                // open modal
                $('#user-modal').modal('show');
            },
            error: (request, message, error) => {
                console.log(request, message, error);
                alert_show('danger', 'User read failed.');
            }
        });
    })
}

// user form read
function user_form_read(user_form) {
    let user = {};
    $(user_form).find('input').each((index, input) => {
        let id = $(input).attr('id');
        if (id && isNaN(id)) {
            if ($(input).attr('type') === 'checkbox') {
                user[id] = $(input).is(':checked');
            } else {
                if ($(input).val() && 0 !== $(input).val().length) {
                    user[id] = isNaN($(input).val()) ? $(input).val() : Number($(input).val());
                }
            }
        }
    });
    user['roles'] = [];
    $(user_form).find('#roles input').each((index, input) => {
        if ($(input).is(':checked')) {
            user['roles'].push({'id': Number($(input).attr('id')), 'name': $(input).val()});
        }
    });
    return user;
}

// user form write
function user_form_write(user_form, user) {
    $(user_form).find('input').each((index, input) => {
        let id = $(input).attr('id');
        if (id && isNaN(id)) {
            if ($(input).attr('type') === 'checkbox') {
                $(input).attr('checked', user[id]);
            } else {
                $(input).attr('value', user[id]);
            }
        }
    });
    $(user_form).find('#roles input').each((index, input) => {
        if (user['roles'].find(role => role.id === Number($(input).attr('id')))) {
            $(input).attr('checked', true);
        } else {
            $(input).attr('checked', false);
        }
    });
}

// user form send request
function user_form_send(user_form, url, type, data, success_handler, error_handler) {
    let csrf = $(user_form).find('input[name="_csrf"]').attr('value');
    $.ajax({
        url: url,
        type: type,
        data: data ? JSON.stringify(data) : undefined,
        dataType: data ? 'json' : undefined,
        headers: {'X-CSRF-TOKEN': csrf},
        contentType: 'application/json',
        success: result => success_handler(result),
        error: (request, message, error) => error_handler(request, message, error)
    });
}

// inject alert message
function alert_show(type, message) {
    const alert = $('#alert');
    const alert_div = '<div class="alert alert-dismissible fade show" role="alert"></div>';
    const alert_button = '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
    let alert_type = 'alert-' + type;
    $(alert).find('.' + alert_type).remove();
    $(alert).append(alert_div);
    let alert_target = $(alert).find('.alert').last();
    $(alert_target).addClass(alert_type).append('<strong>' + (type === 'danger' ? 'error' : type).capitalize() + ':</strong> ' + message + alert_button);
}

// string capitalize method
String.prototype.capitalize = function () {
    return this.charAt(0).toUpperCase() + this.slice(1);
};
