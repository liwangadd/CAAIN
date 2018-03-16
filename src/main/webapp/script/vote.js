let award_id = 1;
let awards_ids = [];
let expert_id = 0;
let fetchUnvoteInterval;

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

    function fetchAwardTypes() {
        $.ajax({
            url: '/awards',
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: (result) => {
                let award_div = $("#awards_btn");
                if (result['code'] !== 'FAILURE') {
                    content = result['content'];
                    award_id = content[0]['id'];
                    fetchEntriesForType(award_id);
                    content.forEach(function (award) {
                        awards_ids.push(award['id']);
                        award_div.append(`<button id="award_btn_${award['id']}" name="${award['id']}" class="btn btn-default award_btn">${award['award_name']}</button>`)
                    });
                    for (let i = 1, len = content.length; i <= len; ++i) {
                        $(`#award_btn_${i}`).on('click', () => {
                            fetchEntriesForType(awards_ids[i - 1]);
                        })
                    }
                }
            }
        });
    }

    $.ajax({
        url: "/expert",
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: (result) => {
            if (result['code'] === 'FAILURE') {
                toastr.options.timeout = 2000;
                toastr.error(result['reason']);
                $('#submitVote').attr('disabled', 'disabled');
            } else {
                fetchAwardTypes();
                expert_id = result['content']['id'];
                if (result['content']['voted'] === 1) {
                    $('#submitVote').attr('disabled', 'disabled');
                }
            }
        }
    });

    function fetchEntriesForType(award_id) {
        $.ajax({
            url: '/entries/' + award_id,
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: (result) => {
                console.log(result);
                if (result['code'] !== 'FAILURE') {
                    let voteBody = $('#voteBody');
                    voteBody.empty();
                    content = result['content'];
                    for (let i = 0, len = content.length; i < len; ++i) {
                        voteBody.append(`<tr>
                        <td id="entryId">${i + 1}</td>
                        <td id="entry_id" hidden>${content[i]['id']}</td>
                        <td id="prize">
                            <select class="form-control">
                                <option>无</option>
                                <option>一等奖</option>
                                <option>二等奖</option>
                                <option>三等奖</option>
                            </select>
                        </td>
                        <td>${content[i]['expert_num']}</td>
                        <td id="entry_name">${content[i]['entry_name']}</td>
                        <td></td>
                    </tr>`)
                    }
                } else {
                    toastr.options.timeout = 2000;
                    toastr.error(result['reason']);
                }
            }
        })
    }

    function fetchUnvotedExpert() {
        $.ajax({
            url: "/unvoted",
            type: 'GET',
            success: (result) => {
                console.log(result);
                $('#rest_div').removeClass("hidden");
                $('#rest_div').addClass('show');

                $('#rest_h2').text(`还剩${result['content']['count']}个专家没有投票`);

                if (result['content']['count'] === 0) {
                    clearInterval(fetchUnvoteInterval);
                    showResult();
                }
            }
        })
    }

    $('#submitVote').on('click', () => {
        Ewin.confirm({message: "确认投票？"}).on(function (e) {
            if (!e) {
                return;
            }
            let vote_result = [];
            $('#voteBody').find('tr').each(function () {
                let level1 = 0, level2 = 0, level3 = 0;
                let prize = $(this).find('option:selected').text();
                let entry_id = $(this).find('#entry_id').text();
                let entry_name = $(this).find('#entry_name').text();
                if (prize === '一等奖') {
                    level1 = 1;
                } else if (prize === '二等奖') {
                    level2 = 1;
                } else if (prize === '三等奖') {
                    level3 = 1;
                }
                let entry_expert = {};
                entry_expert['expert_id'] = expert_id;
                entry_expert['entry_id'] = entry_id;
                entry_expert['level1'] = level1;
                entry_expert['level2'] = level2;
                entry_expert['level3'] = level3;
                entry_expert['entry_prize'] = prize;
                entry_expert['award_id'] = award_id;
                entry_expert['entry_name'] = entry_name;
                vote_result.push(entry_expert);
            });
            console.log(vote_result);
            console.log(JSON.stringify(vote_result));
            $.ajax({
                url: '/vote',
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(vote_result),
                success: (result) => {
                    console.log(result);
                    if (result['code'] === 'SUCCESS') {
                        $('#submitVote').attr('disabled', 'disabled');
                        fetchUnvoteInterval = setInterval(fetchUnvotedExpert, 2000);
                    } else {
                        toastr.options.timeout = 2000;
                        toastr.error(result['reason']);
                    }
                }
            })
        });
    });

    function showResult() {
        $.ajax({
            url: '/vote/result/' + award_id,
            type: 'GET',
            success: (result) => {
                console.log(result);
                if (result['code'] === 'SUCCESS') {
                    let voteThead = $('#voteThead');
                    voteThead.empty();
                    voteThead.append('<tr>\n' +
                        '<th class="text-center">序号</th>\n' +
                        '<th class="text-center">总专家数</th>\n' +
                        '<th class="text-center">文档名称</th>\n' +
                        '<th class="text-center">获奖结果</th>\n' +
                        '</tr>');


                    let voteBody = $("#voteBody");
                    voteBody.empty();
                    content = result['content'];
                    for (let i = 0; i < content.length; i++) {
                        voteBody.append(`<tr>
                                <td id="entryId">${i + 1}</td>
                                <td>11</td>
                                <td id="entryName">${content[i]['entry_name']}</td>
                                <td>${content[i]['prize']}</td>
                            </tr>`)
                    }
                } else {
                    toastr.options.timeout = 2000;
                    toastr.error(result['reason']);
                }
            }
        })
    }

    $('#showResult').on('click', showResult);

});