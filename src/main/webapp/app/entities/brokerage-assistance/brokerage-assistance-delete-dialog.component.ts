import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';
import { BrokerageAssistanceService } from './brokerage-assistance.service';

@Component({
    selector: 'jhi-brokerage-assistance-delete-dialog',
    templateUrl: './brokerage-assistance-delete-dialog.component.html'
})
export class BrokerageAssistanceDeleteDialogComponent {
    brokerageAssistance: IBrokerageAssistance;

    constructor(
        private brokerageAssistanceService: BrokerageAssistanceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.brokerageAssistanceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'brokerageAssistanceListModification',
                content: 'Deleted an brokerageAssistance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-brokerage-assistance-delete-popup',
    template: ''
})
export class BrokerageAssistanceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageAssistance }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BrokerageAssistanceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.brokerageAssistance = brokerageAssistance;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
