$(function () {

    let award_ids = [];

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
        url: '/admin/awards',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: (result) => {
            let award_div = $("#pdf_btn_group");
            let vote_div = $("#vote_btn_group");
            let view_div = $("#vote_result_group");
            if (result['code'] !== 'FAILURE') {
                content = result['content'];
                content.forEach(function (award) {
                    award_ids.push(award['id']);
                    award_div.append(`<button id="award_btn_${award['id']}" class="btn btn-primary" style="margin-left: 8px">${award['award_name'].replace(/^\d-/, '')}PDF</button>`);
                    vote_div.append(`<button id="vote_btn_${award['id']}" class="btn btn-primary" style="margin-left: 8px">开启${award['award_name'].replace(/^\d-/, '')}投票</button>`);
                    view_div.append(`<button id="view_btn_${award['id']}" class="btn btn-primary" style="margin-left: 8px">查看${award['award_name'].replace(/^\\d-/, '')}结果</button>`);

                });
                for (let i = 1, len = content.length; i <= len; ++i) {
                    $(`#award_btn_${i}`).on('click', () => {
                        buildFinalPDF(award_ids[i - 1]);
                    });
                    $(`#vote_btn_${i}`).on('click', ()=>{
                        startVote(award_ids[i-1]);
                    });
                    $(`#view_btn_${i}`).on('click', ()=>{
                        window.location.href="/admin/result";
                    })
                }
            }
        }
    });

    $('#clear_vote').on('click', () => {
        $.ajax({
            url: '/admin/vote/clear',
            type: 'PUT',
            success: (result) => {
                toastr.info(result['reason']);
            }
        })
    });

    function startVote(award_id) {
        $.ajax({
            url: '/admin/start_vote/' + award_id,
            type: 'POST',
            success: (result)=>{
                if(result['code'] === 'SUCCESS'){
                    toastr.info(result['reason']);
                }else{
                    toastr.info(result['reason']);
                }
            }
        })
    }

    function buildFinalPDF(award_id) {
        $.ajax({
            url: '/admin/pdf/' + award_id,
            type: 'POST',
            success: (result) => {
                if (result['code'] === 'SUCCESS') {
                    toastr.info(result['reason']);
                } else {
                    toastr.error(result['reason']);
                }
            }
        })
    }

});