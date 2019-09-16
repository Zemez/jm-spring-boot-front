$(document).ready(function () {
    $.get('/admin/all', function (users) {
        $('#users-content').html(users);
        $('.user').on('click', function (event) {
            event.preventDefault();
            var href = '/admin/' + $(this).attr('id');
            $.get(href, function (user) {
                var username = $(user).find('#username').val();
                $('#user-modal-title').text('Edit user ' + username);
                $('#user-modal-body').html(user);
            });
            $('#user-modal').modal();
        })
    });
    $.get('/admin/create', function (user) {
        $('#create-content').html(user);
    });
});
