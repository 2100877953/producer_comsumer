package com.yzs.view;

import java.awt.EventQueue;

public class MainFrame extends JFrame implements Runnable{
	private ArrayList<Thread> tList;
	private volatile int count=1;
	
	private BufferPool pool=new BufferPool();
	private static volatile String threadInfo=null;
	private static volatile String poolInfo=null;
	private JPanel contentPane;
	private JComboBox pro_jcb;
	private JComboBox con_jcb;
	private JTextArea bufferStatusTextArea;
	private JTextArea threadStatusTextArea;
	
	private JButton byStepButton;
	private JButton aotuButton;
	private JButton clearButton;
	
	private Thread status;
	/**
	 * 运行主程序
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 创建主界面
	 */
	public MainFrame() {
		setResizable(false);
		setTitle("生产者消费者");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 625);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("生产者数量：");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 17));
		
		pro_jcb = new JComboBox();
		pro_jcb.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		pro_jcb.setSelectedItem("1");
		JLabel label = new JLabel("消费者数量：");
		label.setFont(new Font("宋体", Font.PLAIN, 17));
		
		con_jcb = new JComboBox();
		con_jcb.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		pro_jcb.setSelectedItem("1");
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MainFrame.class.getResource("/images/producer.png")));
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(MainFrame.class.getResource("/images/consumer.png")));
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(MainFrame.class.getResource("/images/process.png")));
		
		JLabel lblNewLabel_4 = new JLabel("线程运行状态");
		lblNewLabel_4.setFont(UIManager.getFont("Label.font"));
		
		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setIcon(new ImageIcon(MainFrame.class.getResource("/images/Buffer.png")));
		
		JLabel lblNewLabel_6 = new JLabel("缓冲池资源状态");
		
		byStepButton = new JButton("单步");
		byStepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stepRunActionPerformed(e);
			}
		});
		byStepButton.setIcon(new ImageIcon(MainFrame.class.getResource("/images/step.png")));
		byStepButton.setBackground(Color.WHITE);
		byStepButton.setFont(new Font("宋体", Font.PLAIN, 18));
		
		aotuButton = new JButton("自动");
		aotuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autoRunActionPerformed(e);
			}
		});
		aotuButton.setIcon(new ImageIcon(MainFrame.class.getResource("/images/start.png")));
		aotuButton.setBackground(Color.WHITE);
		aotuButton.setForeground(Color.BLACK);
		aotuButton.setFont(new Font("宋体", Font.PLAIN, 18));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		clearButton = new JButton("清除");
		clearButton.setEnabled(false);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearActionPerformed(e);
			}
		});
		clearButton.setIcon(new ImageIcon(MainFrame.class.getResource("/images/recycle.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(36, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_4))
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel_5)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_6))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 425, GroupLayout.PREFERRED_SIZE))
					.addGap(23))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(154)
							.addComponent(byStepButton)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(aotuButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(184)
							.addComponent(lblNewLabel_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(pro_jcb, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(lblNewLabel_2)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(con_jcb, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(84)
							.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(266, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(59)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(pro_jcb, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addComponent(con_jcb, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)))
					.addGap(75)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(lblNewLabel_6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_3)
								.addComponent(lblNewLabel_5)))
						.addComponent(lblNewLabel_4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE))
					.addGap(51)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(byStepButton, GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE)
						.addComponent(aotuButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(clearButton))
					.addGap(96))
		);
		
		bufferStatusTextArea = new JTextArea();
		bufferStatusTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane_1.setViewportView(bufferStatusTextArea);
		DefaultCaret caret_b = (DefaultCaret)bufferStatusTextArea.getCaret();
		caret_b.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		threadStatusTextArea = new JTextArea();
		threadStatusTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(threadStatusTextArea);
		DefaultCaret caret_t = (DefaultCaret)threadStatusTextArea.getCaret();
		caret_t.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		contentPane.setLayout(gl_contentPane);
	}

	private void stepRunActionPerformed(ActionEvent e) {
		if(count==1){
			clearButton.setEnabled(true);
			count++;
			Producer p = new Producer("step",pool);
			Consumer c = new Consumer("step",pool);
			tList = new ArrayList<>();
			int pro_num = Integer.parseInt((String)pro_jcb.getSelectedItem());
			int con_num = Integer.parseInt((String)con_jcb.getSelectedItem());
			BufferPool.changeStepFlag();
			for(int i=1;i<=pro_num;i++){
				tList.add(new Thread(p, "producer-"+i));
			}
			for(int i=1;i<=con_num;i++){
				
				tList.add(new Thread(c, "consumer-"+i));
			}
			for (Thread thread : tList) {
				thread.start();
			}
			status = new Thread(this);
			status.start();
			aotuButton.setEnabled(false);
		}else {
			BufferPool.changeStepFlag();
		}
	}

	private void clearActionPerformed(ActionEvent e) {
		clearButton.setEnabled(false);
		status.interrupt();
		bufferStatusTextArea.setText(null);
		threadStatusTextArea.setText(null);
		for (Thread thread : tList) {
			thread.interrupt();
		}
		/**
		 * (重要)主线程等待生产者消费者进程退出
		 * 去掉该段代码会造成缓冲池残留数据
		 */
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		pool.getList().clear();
		tList.clear();
		threadInfo=null;
		poolInfo=null;
		BufferPool.stepflag=true;
		aotuButton.setEnabled(true);
		byStepButton.setEnabled(true);
		count=1;
	}

	private void autoRunActionPerformed(ActionEvent e) {
		clearButton.setEnabled(true);
		byStepButton.setEnabled(false);
		Producer p = new Producer("auto",pool);
		Consumer c = new Consumer("auto",pool);
		tList = new ArrayList<>();
		int pro_num = Integer.parseInt((String)pro_jcb.getSelectedItem());
		int con_num = Integer.parseInt((String)con_jcb.getSelectedItem());
		for(int i=1;i<=pro_num;i++){
			
			tList.add(new Thread(p, "producer-"+i));
		}
		for(int i=1;i<=con_num;i++){
			
			tList.add(new Thread(c, "consumer-"+i));
		}
		for (Thread thread : tList) {
			thread.start();
		}
		status = new Thread(this);
		status.start();
		aotuButton.setEnabled(false);
	}

	private void fillBufferTextArea() {
		if(Thread.currentThread().isInterrupted()){
			return;
		}
		if(poolInfo!=null){
			bufferStatusTextArea.append(poolInfo+"\n");
			poolInfo=null;
		}
	}

	private void fillThreadTextArea() {
		if(Thread.currentThread().isInterrupted()){
			return;
		}
		if(threadInfo!=null){
			threadStatusTextArea.append(threadInfo+"\n");
			threadInfo=null;
		}
		
	}

	public static void getMessage(String t_info,String p_info) {
		threadInfo = t_info;
		poolInfo = p_info;
	}

	@Override
	public void run() {
		while(true){
			try {
				fillThreadTextArea();
				fillBufferTextArea();
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
