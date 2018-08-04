let awardId = 1;
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

    $.ajax({
        url: "/vote/data",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: (result) => {
            if (result['code'] === 'FAILURE') {
                toastr.options.timeout = 2000;
                toastr.error(result['reason']);
                $('#submitVote').attr('disabled', 'disabled');
            } else {
                let awards = result['content']['voteAwards'];
                awardId = awards[0]['id'];
                expert_id = result['content']['expert']['id'];
                let expert_count = awards[0]['expert_count'];
                let award_title_h2 = $("#award_title");
                award_title_h2.text("吴文俊人工智能科学技术-" + awards[0]['award_name'].replace(/^\d-/, ''));
                let entries = awards[0]['entries'];
                let voteBody = $('#voteBody');
                voteBody.empty();
                for (let i = 0, len = entries.length; i < len; ++i) {
                    console.log('level ' + i + ' ' + entries[i]['level1']);
                    voteBody.append(`<tr>
                        <td id="entryId">${i + 1}</td>
                        <td id="entry_id" hidden>${entries[i]['id']}</td>
                        <td id="prize">
                            <select class="form-control">
                                <option>无</option>
                                <option ${entries[i]['level1'] === 1 ? 'selected' : null}>一等奖</option>
                                <option ${entries[i]['level2'] === 1 ? 'selected' : null}>二等奖</option>
                                <option ${entries[i]['level3'] === 1 ? 'selected' : null}>三等奖</option>
                            </select>
                        </td>
                        <td>${expert_count}</td>
                        <td id="entry_name">${entries[i]['entry_name'].replace(/^\d-/, '')}</td>
                        <td></td>
                    </tr>`)
                }

                if (result['content']['expert']['voted']) {
                    $('#submitVote').attr('disabled', 'disabled');
                    fetchUnvotedExpert();
                }
            }
        }
    });

    function fetchUnvotedExpert() {
        $.ajax({
            url: "/vote/unvoted",
            type: 'GET',
            success: (result) => {
                console.log(result);
                let unVotedCountView = $("#rest_div");
                unVotedCountView.removeClass("hidden");
                unVotedCountView.addClass('show');

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
                entry_expert['award_id'] = awardId;
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
                        toastr.success(result['reason']);
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
            url: '/vote/result/' + awardId,
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
                                <td>${content[i]['entry_prize']}</td>
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

    // $.ajax({
    //     url: "/expert",
    //     type: 'GET',
    //     contentType: 'application/json',
    //     dataType: 'json',
    //     success: (result) => {
    //         if (result['code'] === 'FAILURE') {
    //             toastr.options.timeout = 2000;
    //             toastr.error(result['reason']);
    //             $('#submitVote').attr('disabled', 'disabled');
    //         } else {
    //             fetchAwardTypes();
    //             expert_id = result['content']['id'];
    //             if (result['content']['voted'] === 1) {
    //                 $('#submitVote').attr('disabled', 'disabled');
    //             }
    //         }
    //     }
    // });

    // function fetchAwardTypes() {
    //     $.ajax({
    //         url: '/awards',
    //         type: 'GET',
    //         contentType: 'application/json',
    //         dataType: 'json',
    //         success: (result) => {
    //             // let award_div = $("#awards_btn");
    //             let award_title_h2 = $("#award_title");
    //             if (result['code'] !== 'FAILURE') {
    //                 content = result['content'];
    //                 awardId = content[0]['id'];
    //                 fetchEntriesForType(awardId);
    //                 content.forEach(function (award) {
    //                     awards_ids.push(award['id']);
    //                     // award_div.append(`<button id="award_btn_${award['id']}" name="${award['id']}" class="btn btn-default award_btn">${award['award_name']}</button>`)
    //                     award_title_h2.text("吴文俊人工智能科学技术-" + award['award_name'].replace(/^\d/, ""));
    //                 });
    //                 for (let i = 1, len = content.length; i <= len; ++i) {
    //                     $(`#award_btn_${i}`).on('click', () => {
    //                         fetchEntriesForType(awards_ids[i - 1]);
    //                     })
    //                 }
    //             } else {
    //                 $('#submitVote').attr('disabled', 'disabled');
    //                 toastr.options.timeout = 2000;
    //                 toastr.error(result['reason']);
    //             }
    //         }
    //     });
    // }

    // function fetchEntriesForType(award_id) {
    //     $.ajax({
    //         url: '/entries/' + award_id,
    //         type: 'GET',
    //         contentType: 'application/json',
    //         dataType: 'json',
    //         success: (result) => {
    //             console.log(result);
    //             if (result['code'] !== 'FAILURE') {
    //                 let voteBody = $('#voteBody');
    //                 voteBody.empty();
    //                 content = result['content'];
    //                 for (let i = 0, len = content.length; i < len; ++i) {
    //                     voteBody.append(`<tr>
    //                     <td id="entryId">${i + 1}</td>
    //                     <td id="entry_id" hidden>${content[i]['id']}</td>
    //                     <td id="prize">
    //                         <select class="form-control">
    //                             <option>无</option>
    //                             <option>一等奖</option>
    //                             <option>二等奖</option>
    //                             <option>三等奖</option>
    //                         </select>
    //                     </td>
    //                     <td>${content[i]['expert_num']}</td>
    //                     <td id="entry_name">${content[i]['entry_name']}</td>
    //                     <td></td>
    //                 </tr>`)
    //                 }
    //                 awardId = award_id;
    //             } else {
    //                 toastr.options.timeout = 2000;
    //                 toastr.error(result['reason']);
    //             }
    //         }
    //     })
    // }

});