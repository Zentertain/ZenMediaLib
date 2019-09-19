void av_base64_decode();
void av_base64_encode();
void av_bitstream_filter_init();
void av_bitstream_filter_close();
void av_bitstream_filter_filter();
void av_buffersink_get_frame();
void av_buffersrc_add_frame_flags();
void av_copy_packet();
void av_dict_count();
void av_dict_get();
void av_dict_set();
void av_dict_free();
void av_dump_format();
void av_find_best_stream();
void av_find_default_stream_index();
void av_find_program_from_stream();
void av_frame_alloc();
void av_frame_clone();
void av_frame_free();
void av_frame_is_writable();
void av_frame_ref();
void av_frame_unref();
void av_free();
void av_freep();
void av_get_bits_per_pixel();
void av_get_bytes_per_sample();
void av_get_channel_layout_nb_channels();
void av_get_codec_tag_string();
void av_get_default_channel_layout();
void av_grow_packet();
void av_guess_frame_rate();
void av_guess_sample_aspect_ratio();
void av_image_copy();
void av_init_packet();
void av_log();
void av_log_set_callback();
void av_malloc();
void av_mallocz();
void av_new_packet();
void av_packet_copy_props();
void av_packet_ref();
void av_packet_unref();
void av_pix_fmt_desc_get();
void av_read_frame();
void av_read_pause();
void av_read_play();
void av_register_all();
void av_rescale();
void av_rescale_q();
void av_rescale_rnd();
void av_sample_fmt_is_planar();
void av_samples_get_buffer_size();
void av_seek_frame();
void av_shrink_packet();
void av_strdup();
void avcodec_close();
void avcodec_decode_audio4();
void avcodec_decode_subtitle2();
void avcodec_decode_video2();
void avcodec_find_decoder();
void avcodec_flush_buffers();
void avcodec_get_name();
// void avcodec_profile_name();
void av_get_profile_name();
void avcodec_open2();
void avcodec_register();
void avformat_alloc_context();
void avformat_close_input();
void avformat_find_stream_info();
void avformat_network_init();
void avformat_open_input();
void avformat_seek_file();
void avfilter_inout_alloc();
void avfilter_inout_free();
void avfilter_get_by_name();
void avfilter_graph_alloc();
void avfilter_graph_config();
void avfilter_graph_create_filter();
void avfilter_graph_free();
void avfilter_graph_parse_ptr();
void avfilter_register_all();
void avio_alloc_context();
void avio_flush();
void avio_size();
void avsubtitle_free();
void ff_check_interrupt();
void ff_add_format();
void ff_isom_write_hvcc();
void ff_isom_write_avcc();
void swr_alloc_set_opts();
void swr_convert();
void swr_free();
void swr_init();
void sws_freeContext();
void sws_getCachedContext();
void sws_scale();
// void ff_draw_init();
// void ff_draw_color();
// void ff_blend_mask();
void avformat_write_header();
void av_interleaved_write_frame();
void av_write_trailer();
void avformat_alloc_output_context2();
void avformat_new_stream();
void avcodec_find_encoder();
void avcodec_copy_context();
void avcodec_encode_audio2();
void avcodec_encode_video2();
void avcodec_register_all();
void av_audio_fifo_alloc();
void av_audio_fifo_size();
void av_audio_fifo_write();
void av_audio_fifo_read();
void av_audio_fifo_free();
void av_strerror();
void av_bprint_init();
void av_bprintf();
void avfilter_link();
void avio_open_dyn_buf();
void av_get_pix_fmt_name();
void avio_printf();
void avio_close_dyn_buf();
void av_opt_set();
void avfilter_graph_parse2();
void av_packet_rescale_ts();
void av_free_packet();
void avfilter_graph_request_oldest();
void av_buffersrc_get_nb_failed_requests();
void av_frame_get_best_effort_timestamp();
void av_buffersrc_add_frame();
void av_guess_codec();
void avcodec_alloc_context3();
void avio_open2();
void avio_closep();
void avformat_free_context();
void avfilter_free();
void av_frame_get_buffer();
void av_audio_fifo_realloc();
void av_copy_packet_side_data();
void sws_getContext();
void avpicture_fill();
void avfilter_graph_alloc_filter();
void av_get_channel_layout_string();
void av_get_sample_fmt_name();
void av_opt_set_q();
void av_opt_set_int();
void avfilter_init_str();
void av_get_audio_frame_duration();
void av_rescale_delta();
void avio_open();
void av_guess_format();
void av_strlcpy();
void av_samples_alloc();
void av_write_frame();
void swr_get_delay();
void av_get_media_type_string();
void avcodec_free_context();
void av_int_list_length_for_size();
void av_opt_set_bin();
void av_frame_get_pkt_duration();
void avcodec_is_open();


extern unsigned char ff_log2_tab[256];

// Dummy function export functions from static libraries.

void dummy_main()
{
    av_base64_decode();
    av_base64_encode();
    av_bitstream_filter_init();
    av_bitstream_filter_close();
    av_bitstream_filter_filter();
    av_buffersink_get_frame();
    av_buffersrc_add_frame_flags();
    av_copy_packet();
    av_dict_count();
    av_dict_get();
    av_dict_set();
    av_dict_free();
    av_dump_format();
    av_find_best_stream();
    av_find_default_stream_index();
    av_find_program_from_stream();
    av_frame_alloc();
    av_frame_clone();
    av_frame_free();
    av_frame_is_writable();
    av_frame_alloc();
    av_frame_unref();
    av_free();
    av_freep();
    av_get_bits_per_pixel();
    av_get_bytes_per_sample();
    av_get_channel_layout_nb_channels();
    av_get_codec_tag_string();
    av_get_default_channel_layout();
    av_grow_packet();
    av_guess_frame_rate();
    av_guess_sample_aspect_ratio();
    av_image_copy();
    av_init_packet();
    av_log();
    av_log_set_callback();
    av_malloc();
    av_mallocz();
    av_new_packet();
    av_packet_copy_props();
    av_packet_ref();
    av_packet_unref();
    av_pix_fmt_desc_get();
    av_read_frame();
    av_read_pause();
    av_read_play();
    av_register_all();
    av_rescale();
    av_rescale_q();
    av_rescale_rnd();
    av_sample_fmt_is_planar();
    av_samples_get_buffer_size();
    av_seek_frame();
    av_shrink_packet();
    av_strdup();
    avcodec_close();
    avcodec_decode_audio4();
    avcodec_decode_subtitle2();
    avcodec_decode_video2();
    avcodec_find_decoder();
    avcodec_flush_buffers();
    avcodec_get_name();
//  avcodec_profile_name();
    av_get_profile_name();
    avcodec_open2();
    avcodec_register();
    avformat_alloc_context();
    avformat_close_input();
    avformat_find_stream_info();
    avformat_network_init();
    avformat_open_input();
    avformat_seek_file();
    avfilter_inout_alloc();
    avfilter_inout_free();
    avfilter_get_by_name();
    avfilter_graph_alloc();
    avfilter_graph_config();
    avfilter_graph_create_filter();
    avfilter_graph_free();
    avfilter_graph_parse_ptr();
    avfilter_register_all();
    avio_alloc_context();
    avio_flush();
    avio_size();
    avsubtitle_free();
    ff_check_interrupt();
    ff_add_format();
    ff_isom_write_hvcc();
    ff_isom_write_avcc();
    swr_alloc_set_opts();
    swr_convert();
    swr_free();
    swr_init();
    sws_freeContext();
    sws_getCachedContext();
    sws_scale();
//  ff_draw_init();
//  ff_draw_color();
//  ff_blend_mask();
    avformat_write_header();
    av_interleaved_write_frame();
    av_write_trailer();
    avformat_alloc_output_context2();
    avformat_new_stream();
    avcodec_find_encoder();
    avcodec_copy_context();
    avcodec_encode_audio2();
    avcodec_encode_video2();
    avcodec_register_all();
    av_audio_fifo_alloc();
    av_audio_fifo_size();
    av_audio_fifo_write();
    av_audio_fifo_read();
    av_audio_fifo_free();
    av_strerror();
    av_bprint_init();
    av_bprintf();
    avfilter_link();
    avio_open_dyn_buf();
    av_get_pix_fmt_name();
    avio_printf();
    avio_close_dyn_buf();
    av_opt_set();
    avfilter_graph_parse2();
    av_packet_rescale_ts();
    av_free_packet();
    avfilter_graph_request_oldest();
    av_buffersrc_get_nb_failed_requests();
    av_frame_get_best_effort_timestamp();
    av_buffersrc_add_frame();
    av_guess_codec();
    avcodec_alloc_context3();
    avio_open2();
    avio_closep();
    avformat_free_context();
    avfilter_free();
    av_frame_get_buffer();
    av_audio_fifo_realloc();
    av_copy_packet_side_data();
    sws_getContext();
    avpicture_fill();
    avfilter_graph_alloc_filter();
    av_get_channel_layout_string();
    av_get_sample_fmt_name();
    av_opt_set_q();
    av_opt_set_int();
    avfilter_init_str();
    av_get_audio_frame_duration();
    av_rescale_delta();
    avio_open();
    av_guess_format();
    av_strlcpy();
    av_samples_alloc();
    av_write_frame();
    swr_get_delay();
    av_get_media_type_string();
    avcodec_free_context();
    av_int_list_length_for_size();
    av_opt_set_bin();
    av_frame_get_pkt_duration();
    avcodec_is_open();
}

