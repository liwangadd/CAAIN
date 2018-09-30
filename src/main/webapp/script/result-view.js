let awardId = 1;
let expert_id = 0;
let fetchUnvoteInterval;
let fetchNoPreVotedInterval;

$(function () {

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-top-center",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "2000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    $.ajax({
        url: "/admin/result/view/1",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: (result) => {
            console.log(result);
            if (result['code'] === 'FAILURE') {
                toastr.options.timeout = 2000;
                toastr.error(result['reason']);
            } else {
                let entries = result['content'];
                console.log(entries);
                let voteBody = $('#voteBody');
                for(let i=0,len=entries.length;i<len;++i){
                    voteBody.append(`<tr>
                        <td id="entryId">${i + 1}</td>
                        <td id="entry_id">${entries[i]['entry_name']}</td>
                        <td id="first_level">${entries[i]['level1']}</td>
                        <td id="second_level">${entries[i]['level2']}</td>
                        <td id="third_level">${entries[i]['level3']}</td>
                    </tr>`)
                }
            }
        }
    });
});