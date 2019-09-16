$.noConflict();
jQuery(document).ready(function ($) {
    $('.user').on('click', function (event) {
        event.preventDefault();
        var href = '/admin/' + $(this).attr('id');
        $.get(href, function (user) {
            $('#user-modal-body').html(user);
        });
        $('#user-modal').modal();
    })
});
